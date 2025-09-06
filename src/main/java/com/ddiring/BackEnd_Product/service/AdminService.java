package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.common.exception.NotFound;
import com.ddiring.BackEnd_Product.common.response.dto.ApiResponseDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminApproveDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminRejectDto;
import com.ddiring.BackEnd_Product.dto.asset.AssetAccountDto;
import com.ddiring.BackEnd_Product.dto.asset.AssetDistributionDto;
import com.ddiring.BackEnd_Product.dto.escrow.AccountRequestDto;
import com.ddiring.BackEnd_Product.dto.escrow.AccountResponseDto;
import com.ddiring.BackEnd_Product.dto.smartcontract.SmartContractDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.entity.ProductPayload;
import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import com.ddiring.BackEnd_Product.external.SmartContractClient;
import com.ddiring.BackEnd_Product.kafka.NotificationPayload;
import com.ddiring.BackEnd_Product.kafka.NotificationProducer;
import com.ddiring.BackEnd_Product.kafka.enums.NotificationType;
import com.ddiring.BackEnd_Product.external.EscrowClient;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import com.ddiring.BackEnd_Product.repository.ProductRequestRepository;
import com.ddiring.BackEnd_Product.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProductRequestRepository prr;
    private final ProductRepository pr;
    private final EscrowClient ec;
    private final ProductService ps;
    private final AssetService as;
    private final S3Service s3;
    private final NotificationProducer notificationProducer;
    private final SmartContractClient bc;

    /* ---------- 승인 ---------- */
    @Transactional
    public void approve(AdminApproveDto dto, String userSeq) {
        ProductRequestEntity pre = prr.findById(dto.getRequestId())
                .orElseThrow(() -> new RuntimeException("요청이 없습니다"));
        if (pre.getRequestStatus() != ProductRequestEntity.RequestStatus.PENDING)
            throw new IllegalStateException("이미 처리된 요청 입니다");

//        // DB에 기존 product 조회 (UPDATE, STOP일 때만 존재)
//        ProductEntity product = null;
//        if (pre.getRequestType() != ProductRequestEntity.RequestType.CREATE &&
//                pre.getPayload().getProjectId() != null) {
//            product = pr.findById(pre.getPayload().getProjectId())
//                    .orElse(null);
//        }
//
//        List<String> oldDocs = (product != null) ? product.getDocument() : new ArrayList<>();
//        List<String> oldImages = (product != null) ? product.getImage() : new ArrayList<>();
//
//        List<String> newDocs = pre.getPayload().getDocument() != null ? pre.getPayload().getDocument() : new ArrayList<>();
//        List<String> newImages = pre.getPayload().getImage() != null ? pre.getPayload().getImage() : new ArrayList<>();
//
//        // 차집합 -> S3 삭제
//        oldDocs.stream().filter(old -> !newDocs.contains(old)).forEach(s3::deleteFile);
//        oldImages.stream().filter(old -> !newImages.contains(old)).forEach(s3::deleteFile);

        // 알림 DTO 담아둘 변수
        NotificationPayload notificationPayload = null;

        switch (pre.getRequestType()) {
            case CREATE -> {
                handleCreate(pre);

                // CREATE Asset 서비스 호출
                ProductEntity pe = pr.findById(pre.getProjectId())
                        .orElseThrow(() -> new IllegalStateException("승인 처리 후 상품 정보를 찾을 수 없습니다"));

                as.sendAssetAccount(
                        AssetAccountDto.builder()
                                .projectId(pe.getProjectId())
                                .title(pe.getTitle())
                                .account(pe.getAccount())
                                .build()
                );

                // ✅ 스마트컨트랙트 배포 요청
                SmartContractDto scDto = SmartContractDto.builder()
                        .projectId(pe.getProjectId())
                        .tokenName("JJoGae")
                        .tokenSymbol("jjo")
                        .totalGoalAmount(pe.getGoalAmount().longValue())
                        .minAmount(pe.getMinInvestment().longValue())
                        .build();

                ApiResponseDto<String> scResponse = bc.requestDeploy(scDto);

                String code = String.valueOf(scResponse.getCode()); // code가 숫자일 수도 있으니까 문자열로
                if (!"SUCCESS".equalsIgnoreCase(code) && !"200".equals(code)) {
                    throw new IllegalStateException("스마트컨트랙트 배포 실패: " + scResponse.getMessage());
                }

                notificationPayload = new NotificationPayload(
                        List.of(pre.getUserSeq()),
                        NotificationType.INFORMATION.name(),
                        "상품 등록 승인",
                        "상품("+pre.getPayload().getTitle()+") 등록이 승인되었습니다"
                );
            }

            case UPDATE -> {
                handleUpdate(pre);

                // UPDATE Asset 서비스 호출 (제목만 업데이트)
                ProductEntity pe = pr.findById(pre.getProjectId())
                        .orElseThrow(() -> new IllegalStateException("승인 처리 후 상품 정보를 찾을 수 없습니다"));

                as.sendAssetAccount(
                        AssetAccountDto.builder()
                                .projectId(pe.getProjectId())
                                .title(pe.getTitle())
                                .account(pe.getAccount())
                                .build()
                );

                notificationPayload = new NotificationPayload(
                        List.of(pre.getUserSeq()),
                        NotificationType.INFORMATION.name(),
                        "상품 수정 승인",
                        "상품("+pre.getPayload().getTitle()+") 수정이 승인되었습니다"
                );
            }

            case STOP -> {
                handleStop(pre);

                notificationPayload = new NotificationPayload(
                        List.of(pre.getUserSeq()),
                        NotificationType.INFORMATION.name(),
                        "상품 정지 승인",
                        "상품("+pre.getPayload().getTitle()+") 정지가 승인되었습니다"
                );
            }

            case DISTRIBUTION -> {
                handleDistribution(pre);

                // DISTRIBUTION Asset 서비스 호출
                ProductEntity pe = pr.findById(pre.getProjectId())
                        .orElseThrow(() -> new IllegalStateException("승인 처리 후 상품 정보를 찾을 수 없습니다"));

                as.sendAssetDistribution(
                        AssetDistributionDto.builder()
                                .projectId(pe.getProjectId())
                                .userSeq(pe.getUserSeq())
                                .distributionAmount(pe.getDistributionAmount())
                                .build()
                );

                notificationPayload = new NotificationPayload(
                        List.of(pre.getUserSeq()),
                        NotificationType.INFORMATION.name(),
                        "상품 분배 승인",
                        "상품("+ pre.getPayload().getTitle()+") 분배가 승인되었습니다."
                );
            }
        }

        pre.setRequestStatus(ProductRequestEntity.RequestStatus.APPROVED);
        pre.setAdminSeq(userSeq);
        prr.save(pre);

        // 🔔 DB 저장 끝난 후 → 알림 발행
        if (notificationPayload != null) {
            notificationProducer.sendNotification(
                    notificationPayload.getUserSeq(),
                    notificationPayload.getNotificationType(),
                    notificationPayload.getTitle(),
                    notificationPayload.getMessage()
            );
        }
    }

    /* ---------- 거절 ---------- */
    @Transactional
    public void reject(AdminRejectDto dto, String userSeq) {
        ProductRequestEntity pre = prr.findById(dto.getRequestId())
                .orElseThrow(() -> new RuntimeException("요청이 없습니다"));
        if (pre.getRequestStatus() != ProductRequestEntity.RequestStatus.PENDING)
            throw new IllegalStateException("이미 처리된 요청 입니다");

//        ProductEntity product = null;
//        if (pre.getPayload().getProjectId() != null) {
//            product = pr.findById(pre.getPayload().getProjectId())
//                    .orElse(null);
//        }
//
//        List<String> oldDocs = (product != null) ? product.getDocument() : new ArrayList<>();
//        List<String> oldImages = (product != null) ? product.getImage() : new ArrayList<>();
//
//        // 요청에서 넘어온 신규 파일들
//        List<String> newDocs = pre.getPayload().getDocument() != null ? pre.getPayload().getDocument() : new ArrayList<>();
//        List<String> newImages = pre.getPayload().getImage() != null ? pre.getPayload().getImage() : new ArrayList<>();
//
//        // DB에 없는 신규 업로드만 삭제
//        newDocs.stream()
//                .filter(url -> !oldDocs.contains(url))
//                .forEach(s3::deleteFile);
//
//        newImages.stream()
//                .filter(url -> !oldImages.contains(url))
//                .forEach(s3::deleteFile);

        pre.setRequestStatus(ProductRequestEntity.RequestStatus.REJECTED);
        pre.setAdminSeq(userSeq);
        pre.setRejectReason(dto.getRejectReason());
        prr.save(pre);

        // 🔔 거절 알림 발행
        notificationProducer.sendNotification(
                List.of(pre.getUserSeq()),
                NotificationType.INFORMATION.name(),
                "상품 거절",
                "상품("+pre.getPayload().getTitle()+")에 대한 요청이 거절되었습니다. 사유: " + dto.getRejectReason()
        );
    }

    /* ---------- 숨김 ---------- */
    @Transactional
    public ProductEntity.ProjectVisibility toggleVisibility(String projectId, String reason, String adminSeq) {
        ProductEntity pe = pr.findById(projectId)
                .orElseThrow(() -> new NotFound("상품이 없습니다: " + projectId));

        // 종료된 상품은 숨김 토글 불가
        if (pe.getProjectStatus() == ProductEntity.ProjectStatus.CLOSED) {
            throw new IllegalStateException("이미 종료된 상품은 숨김 토글할 수 없습니다");
        }

        boolean goingToHold = (pe.getProjectVisibility() != ProductEntity.ProjectVisibility.HOLD);

        if (goingToHold) { // PUBLIC → HOLD
            if (reason == null || reason.isBlank()) {
                throw new IllegalArgumentException("HOLD 사유는 필수입니다");
            }
            pe.setProjectVisibility(ProductEntity.ProjectVisibility.HOLD);
            pe.setHoldReason(reason.trim()); // 숨김 사유
        } else { // HOLD → PUBLIC
            pe.setProjectVisibility(ProductEntity.ProjectVisibility.PUBLIC);
            pe.setHoldReason(null); // 필요시 해제 사유를 별도 필드로 남겨도 됨
        }

        pe.setHoldAdminSeq(adminSeq);
        pr.save(pe);

        // 🔔 숨김/공개 알림 발행
        if (goingToHold) {
            notificationProducer.sendNotification(
                    List.of(pe.getUserSeq()),
                    NotificationType.INFORMATION.name(),
                    "상품 숨김",
                    "관리자에 의해 상품("+pe.getTitle()+")이 숨김 처리되었습니다. 사유: " + reason
            );
        } else {
            notificationProducer.sendNotification(
                    List.of(pe.getUserSeq()),
                    NotificationType.INFORMATION.name(),
                    "상품 공개",
                    "관리자에 의해 상품("+pe.getTitle()+")이 다시 공개되었습니다."
            );
        }

        return pe.getProjectVisibility();
    }

    /* ---------- 내부로직 ------- */
    private void handleCreate(ProductRequestEntity pre) {
        ProductPayload pp = pre.getPayload();

        // 1. ProductEntity 생성
        ProductEntity pe = ProductEntity.builder()
                .projectId(pre.getProjectId())  // 승인 시 사용될 ID 할당
                .userSeq(pre.getUserSeq())
                .nickname(pp.getNickname())
                .title(pp.getTitle())
                .summary(pp.getSummary())
                .content(pp.getContent())
                .startDate(pp.getStartDate())
                .endDate(pp.getEndDate())
                .goalAmount(pp.getGoalAmount())
                .minInvestment(pp.getMinInvestment())
                .document(new ArrayList<>(pp.getDocument()))
                .image(new ArrayList<>(pp.getImage()))
                .projectStatus(ProductEntity.ProjectStatus.OPEN)
                .projectVisibility(ProductEntity.ProjectVisibility.PUBLIC)
                .version(1L)
                .build();

        // 2. 상품 DB insert (projectId 발급)
        pe = pr.insert(pe);

        // 3. 요청 엔티티에 projectId 연결
        pre.setProjectId(pe.getProjectId());

        // 4. 마감일 계산
        pe.setDeadline(pe.dDay());

        try {
            // 5. 에스크로 계좌 생성
            AccountRequestDto escrowRequest = AccountRequestDto.builder()
                    .projectId(pe.getProjectId())
                    .build();

            AccountResponseDto escrowResponse = ec.createAccount(escrowRequest);

            if (escrowResponse == null || escrowResponse.getAccount() == null) {
                throw new IllegalStateException("에스크로 계좌 생성 실패: account=null");
            }

            // 6. 계좌 세팅 후 저장
            pe.setAccount(escrowResponse.getAccount());
            pr.save(pe);

        } catch (Exception e) {
            // 예외 발생 시 상품 삭제 후 다시 예외 던짐
            pr.deleteById(pe.getProjectId());
            throw new IllegalStateException("상품 생성 중 에스크로 계좌 생성 실패: " + e.getMessage(), e);
        }
    }

    private void handleUpdate(ProductRequestEntity pre) {
            ProductPayload pp = pre.getPayload();
            ProductEntity pe = pr.findById(pp.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다"));

            pe.setTitle(pp.getTitle());
            pe.setSummary(pp.getSummary());
            pe.setContent(pp.getContent());
            pe.setStartDate(pp.getStartDate());
            pe.setEndDate(pp.getEndDate());
            pe.setDeadline(Math.max(pe.dDay(), 0));
            pe.setGoalAmount(pp.getGoalAmount());
            pe.setMinInvestment(pp.getMinInvestment());
            pe.setDocument(new ArrayList<>(pp.getDocument()));
            pe.setImage(new ArrayList<>(pp.getImage()));
            pe.setReason(pp.getReason());
            pe.setProjectStatus(ProductEntity.ProjectStatus.OPEN);
            pe.setProjectVisibility(ProductEntity.ProjectVisibility.PUBLIC);
        pr.save(pe);
    }

    private void handleStop(ProductRequestEntity pre) {
        ProductPayload pp = pre.getPayload();
        ProductEntity pe = pr.findById(pp.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다"));

        pe.setDocument(pe.getDocument());
        pe.setDocument(new ArrayList<>(pp.getDocument()));
        pe.setImage(new ArrayList<>(pp.getImage()));
        pe.setReason(pp.getReason());
        pe.setProjectStatus(ProductEntity.ProjectStatus.TEMPORARY_STOP);
        pe.setProjectVisibility(ProductEntity.ProjectVisibility.PUBLIC);
        pr.save(pe);
    }

    private void handleDistribution(ProductRequestEntity pre) {
        ProductPayload pp = pre.getPayload();
        ProductEntity pe = pr.findById(pp.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다"));

        pe.setDocument(new ArrayList<>(pp.getDocument()));
        pe.setImage(new ArrayList<>(pp.getImage()));
        pe.setDistributionSummary(pp.getDistributionSummary());
        pe.setProjectStatus(ProductEntity.ProjectStatus.DISTRIBUTION_READY);
        pe.setProjectVisibility(ProductEntity.ProjectVisibility.PUBLIC);
        pr.save(pe);
    }
}