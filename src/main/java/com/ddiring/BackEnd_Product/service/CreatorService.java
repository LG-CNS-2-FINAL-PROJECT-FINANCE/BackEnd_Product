package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.CreatorUpdateDto;
import com.ddiring.BackEnd_Product.dto.CreatorCreateDto;
import com.ddiring.BackEnd_Product.dto.CreatorStopDto;
import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import com.ddiring.BackEnd_Product.repository.ProductRequestRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreatorService {

    private final ProductRequestRepository prr;

    public String create(CreatorCreateDto dto, int userSeq) {
        ProductRequestEntity pre = ProductRequestEntity.builder()
                .type(ProductRequestEntity.RequestType.CREATE)
                .status(ProductRequestEntity.RequestStatus.PENDING)
                .payload(dto.toPayload())
                .userSeq(userSeq)
                .build();
        return prr.save(pre).getRequestId();
    }

    public String update (CreatorUpdateDto dto, int userSeq) {
        boolean pending = prr.existsByProductIdAndStatus(dto.getProductId(), ProductRequestEntity.RequestStatus.PENDING);
        if (!pending) throw new IllegalStateException("이미 대기 중인 요청이 있습니다");

        ProductRequestEntity pre = ProductRequestEntity.builder()
                .productId(dto.getProductId())
                .type(ProductRequestEntity.RequestType.UPDATE)
                .status(ProductRequestEntity.RequestStatus.PENDING)
                .payload(dto.toPayload())
                .userSeq(userSeq)
                .build();
        return prr.save(pre).getRequestId();
    }

    public String stop(CreatorStopDto dto, int userSeq) {
        ProductRequestEntity pre = ProductRequestEntity.builder()
                .type(ProductRequestEntity.RequestType.STOP)
                .status(ProductRequestEntity.RequestStatus.PENDING)
                .payload(dto.toPayload())
                .userSeq(userSeq)
                .build();
        return prr.save(pre).getRequestId();
    }
}
