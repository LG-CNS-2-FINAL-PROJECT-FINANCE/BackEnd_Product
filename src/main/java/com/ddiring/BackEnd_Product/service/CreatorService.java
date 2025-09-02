package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.creator.CreatorDistributionDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorUpdateDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorCreateDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorStopDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.entity.ProductPayload;
import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import com.ddiring.BackEnd_Product.repository.ProductRequestRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class CreatorService {

    private final ProductRequestRepository prr;
    private final ProductRepository pr;
    private final RequestService rs;

    /* ---------- 등록요청 ---------- */
    public String create(CreatorCreateDto dto, String userSeq) {

        if (dto == null)
            throw new IllegalArgumentException("요청 데이터가 없습니다.");
        if (dto.getStartDate().isAfter(dto.getEndDate()))
            throw new IllegalArgumentException("시작일이 종료일보다 늦을 수 없습니다.");
        if (dto.getGoalAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("목표 금액은 0보다 커야 합니다.");

        try {
            ProductRequestEntity pre = ProductRequestEntity.builder()
                    .requestType(ProductRequestEntity.RequestType.CREATE)
                    .requestStatus(ProductRequestEntity.RequestStatus.PENDING)
                    .payload(dto.toPayload())
                    .userSeq(userSeq)
                    .build();
            return prr.save(pre).getRequestId();
        } catch (DataAccessException e) {
            throw new RuntimeException("등록 요청 저장 중 오류가 발생했습니다.", e);
        }
    }

    /* ---------- 부분수정요청 ---------- */
    public String update(CreatorUpdateDto dto, String userSeq) {

        // ① 동시에 진행 중인 요청 확인
        if (prr.existsByProjectIdAndRequestStatus(dto.getProjectId(),
                ProductRequestEntity.RequestStatus.PENDING))
            throw new IllegalStateException("이미 대기 중인 요청이 있습니다");

        // ② 원본 상품 스냅샷
        ProductEntity product = pr.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("상품이 없습니다"));

        ProductPayload payload = ProductPayload.from(product);
        payload.update(dto);   // 텍스트 정보 덮어쓰기

        // ④ 요청 엔티티 저장 (승인 대기)
        ProductRequestEntity pre = ProductRequestEntity.builder()
                .projectId(dto.getProjectId())
                .requestType(ProductRequestEntity.RequestType.UPDATE)
                .requestStatus(ProductRequestEntity.RequestStatus.PENDING)
                .payload(payload)
                .userSeq(userSeq)
                .build();

        return prr.save(pre).getRequestId();
    }

    /* ---------- 정지요청 ---------- */
    public String stop(CreatorStopDto dto, String userSeq) {

        // ① 동시에 진행 중인 요청 확인
        if (prr.existsByProjectIdAndRequestStatus(dto.getProjectId(),
                ProductRequestEntity.RequestStatus.PENDING))
            throw new IllegalStateException("이미 대기 중인 요청이 있습니다");

        // ② 원본 상품 스냅샷
        ProductEntity product = pr.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("상품이 없습니다"));

        ProductPayload payload = ProductPayload.from(product);
        payload.stop(dto);   // 텍스트 정보 덮어쓰기

        // ④ 요청 엔티티 저장 (승인 대기)
        ProductRequestEntity pre = ProductRequestEntity.builder()
                .projectId(dto.getProjectId())
                .requestType(ProductRequestEntity.RequestType.STOP)
                .requestStatus(ProductRequestEntity.RequestStatus.PENDING)
                .payload(payload)
                .userSeq(userSeq)
                .build();
        return prr.save(pre).getRequestId();
    }

    /* ---------- 분배요청 ---------- */
    public String distribution(CreatorDistributionDto dto, String userSeq) {
        // ① 동시에 진행 중인 요청 확인
        if (prr.existsByProjectIdAndRequestStatus(dto.getProjectId(),
                ProductRequestEntity.RequestStatus.PENDING)) {
            throw new IllegalStateException("이미 대기 중인 요청이 있습니다");
        }

        // ② 원본 상품 스냅샷
        ProductEntity product = pr.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("상품이 없습니다"));

        if (product.getProjectStatus() != ProductEntity.ProjectStatus.TRADING) {
            throw new IllegalStateException("분배 요청은 TRADING 상태에서만 가능합니다");
        }

        ProductPayload payload = ProductPayload.from(product);
        payload.distribution(dto);   // 텍스트 정보 덮어쓰기

        BigDecimal percent = rs.DistributionPercent(
                payload.getDistributionAmount(), payload.getGoalAmount());
        payload.setDistributionPercent(percent);

        // ③ 요청 엔티티 저장 (승인 대기)
        ProductRequestEntity pre = ProductRequestEntity.builder()
                .projectId(dto.getProjectId())
                .requestType(ProductRequestEntity.RequestType.DISTRIBUTION)
                .requestStatus(ProductRequestEntity.RequestStatus.PENDING)
                .payload(payload)
                .userSeq(userSeq)
                .build();

        return prr.save(pre).getRequestId();
    }
}
