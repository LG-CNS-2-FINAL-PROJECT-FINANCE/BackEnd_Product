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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreatorService {

    private final ProductRequestRepository prr;
    private final ProductRepository pr;

    public String create(CreatorCreateDto dto, int userSeq) {
        ProductRequestEntity pre = ProductRequestEntity.builder()
                .type(ProductRequestEntity.RequestType.CREATE)
                .status(ProductRequestEntity.RequestStatus.PENDING)
                .payload(dto.toPayload())
                .userSeq(userSeq)
                .build();
        return prr.save(pre).getRequestId();
    }

    /* ---------- 부분수정 ---------- */
    public String update(CreatorUpdateDto dto, int userSeq) {
        // ① 동시에 진행 중인 요청 확인 (기존 로직)
        if (prr.existsByProductIdAndStatus(dto.getProductId(),
                ProductRequestEntity.RequestStatus.PENDING))
            throw new IllegalStateException("이미 대기 중인 요청이 있습니다");

        // ② 원본 상품 스냅샷
        ProductEntity product = pr.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("상품이 없습니다"));
        ProductPayload payload = ProductPayload.from(product);
        payload.update(dto);   // 변경치만 덮어쓰기

        // ③ 요청 엔티티 저장
        ProductRequestEntity pre = ProductRequestEntity.builder()
                .productId(dto.getProductId())
                .type(ProductRequestEntity.RequestType.UPDATE)
                .status(ProductRequestEntity.RequestStatus.PENDING)
                .payload(payload)
                .userSeq(userSeq)
                .build();
        return prr.save(pre).getRequestId();
    }

//    public String update (CreatorUpdateDto dto, int userSeq) {
//        boolean pending = prr.existsByProductIdAndStatus(dto.getProductId(), ProductRequestEntity.RequestStatus.PENDING);
//        if (pending) throw new IllegalStateException("이미 대기 중인 요청이 있습니다");
//
//        ProductRequestEntity pre = ProductRequestEntity.builder()
//                .productId(dto.getProductId())
//                .type(ProductRequestEntity.RequestType.UPDATE)
//                .status(ProductRequestEntity.RequestStatus.PENDING)
//                .payload(dto.toPayload())
//                .userSeq(userSeq)
//                .build();
//        return prr.save(pre).getRequestId();
//    }

    /* ---------- 정지 ---------- */
    public String stop(CreatorStopDto dto, int userSeq) {
        if (prr.existsByProductIdAndStatus(dto.getProductId(),
                ProductRequestEntity.RequestStatus.PENDING))
            throw new IllegalStateException("이미 대기 중인 요청이 있습니다");

        ProductEntity product = pr.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("상품이 없습니다"));
        ProductPayload payload = ProductPayload.from(product);
        payload.stop(dto);

        ProductRequestEntity pre = ProductRequestEntity.builder()
                .productId(dto.getProductId())
                .type(ProductRequestEntity.RequestType.STOP)
                .status(ProductRequestEntity.RequestStatus.PENDING)
                .payload(payload)
                .userSeq(userSeq)
                .build();
        return prr.save(pre).getRequestId();
    }

//    public String stop(CreatorStopDto dto, int userSeq) {
//        boolean pending = prr.existsByProductIdAndStatus(dto.getProductId(), ProductRequestEntity.RequestStatus.PENDING);
//        if (pending) throw new IllegalStateException("이미 대기 중인 요청이 있습니다");
//
//        ProductRequestEntity pre = ProductRequestEntity.builder()
//                .type(ProductRequestEntity.RequestType.STOP)
//                .status(ProductRequestEntity.RequestStatus.PENDING)
//                .payload(dto.toPayload())
//                .userSeq(userSeq)
//                .build();
//        return prr.save(pre).getRequestId();
//    }
}
