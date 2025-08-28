package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.common.exception.NotFound;
import com.ddiring.BackEnd_Product.dto.admin.AdminApproveDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminRejectDto;
import com.ddiring.BackEnd_Product.dto.asset.AssetRequestDto;
import com.ddiring.BackEnd_Product.dto.escrow.AccountRequestDto;
import com.ddiring.BackEnd_Product.dto.escrow.AccountResponseDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.entity.ProductPayload;
import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import com.ddiring.BackEnd_Product.external.EscrowClient;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import com.ddiring.BackEnd_Product.repository.ProductRequestRepository;
import com.ddiring.BackEnd_Product.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProductRequestRepository prr;
    private final ProductRepository pr;
    private final EscrowClient ec;
    private final ProductService ps;
    private final S3Service s3;

    /* ---------- 승인 ---------- */
    @Transactional
    public void approve(AdminApproveDto dto, String userSeq) {
        ProductRequestEntity pre = prr.findById(dto.getRequestId())
                .orElseThrow(() -> new RuntimeException("요청이 없습니다"));
        if (pre.getStatus() != ProductRequestEntity.RequestStatus.PENDING)
            throw new IllegalStateException("이미 처리된 요청 입니다");

        // DB에 기존 product 조회 (UPDATE, STOP일 때만 존재)
        ProductEntity product = null;
        if (pre.getType() != ProductRequestEntity.RequestType.CREATE &&
                pre.getPayload().getProjectId() != null) {
            product = pr.findById(pre.getPayload().getProjectId())
                    .orElse(null);
        }

        List<String> oldDocs = (product != null) ? product.getDocument() : new ArrayList<>();
        List<String> oldImages = (product != null) ? product.getImage() : new ArrayList<>();

        List<String> newDocs = pre.getPayload().getDocument() != null ? pre.getPayload().getDocument() : new ArrayList<>();
        List<String> newImages = pre.getPayload().getImage() != null ? pre.getPayload().getImage() : new ArrayList<>();

        // 차집합 -> S3 삭제
        oldDocs.stream().filter(old -> !newDocs.contains(old)).forEach(s3::deleteFile);
        oldImages.stream().filter(old -> !newImages.contains(old)).forEach(s3::deleteFile);

        switch (pre.getType()) {
//            case CREATE -> handleCreate(pre);
            case CREATE -> {
                handleCreate(pre);

                // CREATE일 때만 Asset 서비스 호출
                ProductEntity pe = pr.findById(pre.getProjectId())
                        .orElseThrow(() -> new IllegalStateException("승인 처리 후 상품 정보를 찾을 수 없습니다"));

                ps.sendAsset(
                        AssetRequestDto.builder()
                                .projectId(pe.getProjectId())
                                .title(pe.getTitle())
                                .account(pe.getAccount())
                                .build()
                );
            }
            case UPDATE -> {
                handleUpdate(pre);
                ProductEntity pe = pr.findById(pre.getProjectId())
                        .orElseThrow(() -> new IllegalStateException("승인 처리 후 상품 정보를 찾을 수 없습니다"));

                ps.sendAsset(
                        AssetRequestDto.builder()
                                .projectId(pe.getProjectId())
                                .title(pe.getTitle())
                                .account(pe.getAccount())
                                .build()
                );
            }
            case STOP -> handleStop(pre);
        }

        pre.setStatus(ProductRequestEntity.RequestStatus.APPROVED);
        pre.setAdminSeq(userSeq);
        prr.save(pre);

//        ProductEntity pe = pr.findById(pre.getProjectId())
//                .orElseThrow(() -> new IllegalStateException("승인 처리 후 상품 정보를 찾을 수 없습니다"));
//
//        ps.sendAsset(
//                AssetRequestDto.builder()
//                        .projectId(pre.getProjectId())
//                        .account(pe.getAccount())
//                        .build()
//        );
    }

    /* ---------- 거절 ---------- */
    @Transactional
    public void reject(AdminRejectDto dto, String userSeq) {
        ProductRequestEntity pre = prr.findById(dto.getRequestId())
                .orElseThrow(() -> new RuntimeException("요청이 없습니다"));
        if (pre.getStatus() != ProductRequestEntity.RequestStatus.PENDING)
            throw new IllegalStateException("이미 처리된 요청 입니다");

        ProductEntity product = null;
        if (pre.getPayload().getProjectId() != null) {
            product = pr.findById(pre.getPayload().getProjectId())
                    .orElse(null);
        }

        List<String> oldDocs = (product != null) ? product.getDocument() : new ArrayList<>();
        List<String> oldImages = (product != null) ? product.getImage() : new ArrayList<>();

        // 요청에서 넘어온 신규 파일들
        List<String> newDocs = pre.getPayload().getDocument() != null ? pre.getPayload().getDocument() : new ArrayList<>();
        List<String> newImages = pre.getPayload().getImage() != null ? pre.getPayload().getImage() : new ArrayList<>();

        // DB에 없는 신규 업로드만 삭제
        newDocs.stream()
                .filter(url -> !oldDocs.contains(url))
                .forEach(s3::deleteFile);

        newImages.stream()
                .filter(url -> !oldImages.contains(url))
                .forEach(s3::deleteFile);

        pre.setStatus(ProductRequestEntity.RequestStatus.REJECTED);
        pre.setAdminSeq(userSeq);
        pre.setRejectReason(dto.getRejectReason());
        prr.save(pre);
    }

    /* ---------- 숨김 ---------- */
    @Transactional
    public ProductEntity.ProductStatus toggleHold(String projectId, String reason, String adminSeq) {
        ProductEntity pe = pr.findById(projectId)
                .orElseThrow(() -> new NotFound("상품이 없습니다: " + projectId));

        if (pe.getStatus() == ProductEntity.ProductStatus.END) {
            throw new IllegalStateException("이미 마감된 상품은 HOLD 토글할 수 없습니다");
        }

        boolean goingToHold = (pe.getStatus() != ProductEntity.ProductStatus.HOLD);

        if (goingToHold) { // OPEN/STOPPED 등 -> HOLD
            if (reason == null || reason.isBlank()) {
                throw new IllegalArgumentException("HOLD 사유는 필수입니다");
            }
            pe.setStatus(ProductEntity.ProductStatus.HOLD);
            // 기존 reason 필드는 '현재 상태의 사유'로 사용
            pe.setReason(reason.trim());
        } else { // HOLD -> OPEN or END (UNHOLD)
            LocalDate today = LocalDate.now();
            if (pe.getEndDate() != null && pe.getEndDate().isBefore(today)) {
                pe.setStatus(ProductEntity.ProductStatus.END);
            } else {
                pe.setStatus(ProductEntity.ProductStatus.OPEN);
                pe.setDeadline(pe.dDay());
            }
            // 해제 사유도 남기고 싶다면 '현재 상태 사유'를 비우되, 메타에는 저장
            pe.setReason(null);
        }
        pr.save(pe);
        return pe.getStatus();
    }

    /* ---------- 내부로직 ------- */
    private void handleCreate(ProductRequestEntity pre) {
        ProductPayload pp = pre.getPayload();
        ProductEntity pe = ProductEntity.builder()
                .projectId(pre.getProjectId())  // 승인 시 사용될 ID 할당
                .userSeq(pre.getUserSeq())
                .title(pp.getTitle())
                .summary(pp.getSummary())
                .content(pp.getContent())
                .startDate(pp.getStartDate())
                .endDate(pp.getEndDate())
                .goalAmount(pp.getGoalAmount())
                .minInvestment(pp.getMinInvestment())
                .document(new ArrayList<>(pp.getDocument()))
                .image(new ArrayList<>(pp.getImage()))
                .status(ProductEntity.ProductStatus.OPEN)
                .version(1L)
                .build();
        pr.insert(pe);
        pre.setProjectId(pe.getProjectId()); // 요청 entity에 projectId 연결

        //마감기일
        pe.setDeadline(pe.dDay());

        //Escrow 계좌
        AccountRequestDto escrowRequest = AccountRequestDto.builder()
                .projectId(pe.getProjectId())
                .build();
        AccountResponseDto escrowResponse = ec.createAccount(escrowRequest);
        pe.setAccount(escrowResponse.getAccount());

//        //SmartContract 주소
//        SmartContractRequestDto smartContractRequest = SmartContractRequestDto.builder()
//                .projectId(pe.getProjectId())
//                .build();
//        SmartContractResponseDto smartContractResponse = scc.createSmartContract(smartContractRequest);
//        pe.setSmartContract(smartContractResponse.getSmartContract());

        pr.save(pe); // 다시 저장해서 계좌 반영
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
            pe.setStatus(ProductEntity.ProductStatus.OPEN);
        pr.save(pe);
    }

    private void handleStop(ProductRequestEntity pre) {
        ProductPayload pp = pre.getPayload();
        ProductEntity pe = pr.findById(pp.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다"));
        pe.setDocument(new ArrayList<>(pp.getDocument()));
        pe.setImage(new ArrayList<>(pp.getImage()));
        pe.setReason(pp.getReason());
        pe.setStatus(ProductEntity.ProductStatus.HOLD);
        pr.save(pe);
    }
}