package com.ddiring.BackEnd_Product.service;

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

    /* ---------- 등록요청 ---------- */
    public String create(CreatorCreateDto dto, String userSeq) {

        if (dto == null) {
            throw new IllegalArgumentException("요청 데이터가 없습니다.");
        }
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("시작일이 종료일보다 늦을 수 없습니다.");
        }
        if (dto.getGoalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("목표 금액은 0보다 커야 합니다.");
        }

        try {
            ProductRequestEntity pre = ProductRequestEntity.builder()
                    .type(ProductRequestEntity.RequestType.CREATE)
                    .status(ProductRequestEntity.RequestStatus.PENDING)
                    .payload(dto.toPayload())
                    .userSeq(userSeq)
                    .build();
            return prr.save(pre).getRequestId();
        } catch (DataAccessException e) {
            throw new RuntimeException("등록 요청 저장 중 오류가 발생했습니다.", e);
        }
    }

    /* ---------- 부분수정 ---------- */
    public String update(CreatorUpdateDto dto, String userSeq) {
        // ① 동시에 진행 중인 요청 확인 (기존 로직)
        if (prr.existsByProjectIdAndStatus(dto.getProjectId(),
                ProductRequestEntity.RequestStatus.PENDING))
            throw new IllegalStateException("이미 대기 중인 요청이 있습니다");

        // ② 원본 상품 스냅샷
        ProductEntity product = pr.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("상품이 없습니다"));
        ProductPayload payload = ProductPayload.from(product);
        payload.update(dto);   // 변경치만 덮어쓰기

        // ③ 요청 엔티티 저장
        ProductRequestEntity pre = ProductRequestEntity.builder()
                .projectId(dto.getProjectId())
                .type(ProductRequestEntity.RequestType.UPDATE)
                .status(ProductRequestEntity.RequestStatus.PENDING)
                .payload(payload)
                .userSeq(userSeq)
                .build();
        return prr.save(pre).getRequestId();
    }

    /* ---------- 정지 ---------- */
    public String stop(CreatorStopDto dto, String userSeq) {
        if (prr.existsByProjectIdAndStatus(dto.getProjectId(),
                ProductRequestEntity.RequestStatus.PENDING))
            throw new IllegalStateException("이미 대기 중인 요청이 있습니다");

        ProductEntity product = pr.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("상품이 없습니다"));
        ProductPayload payload = ProductPayload.from(product);
        payload.stop(dto);

        ProductRequestEntity pre = ProductRequestEntity.builder()
                .projectId(dto.getProjectId())
                .type(ProductRequestEntity.RequestType.STOP)
                .status(ProductRequestEntity.RequestStatus.PENDING)
                .payload(payload)
                .userSeq(userSeq)
                .build();
        return prr.save(pre).getRequestId();
    }
}
