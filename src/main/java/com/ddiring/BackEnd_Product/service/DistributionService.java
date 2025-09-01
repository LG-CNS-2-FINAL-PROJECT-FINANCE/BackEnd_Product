package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.escrow.EscrowDistributionDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DistributionService {

    private final ProductRepository pr;

    @Transactional
    public void handleDeposit(EscrowDistributionDto dto) {
        ProductEntity pe = pr.findByAccount(dto.getAccount())
                .orElseThrow(() -> new IllegalArgumentException("상품 없음: " + dto.getAccount()));

        // 분배 준비 상태에서만 동작
        if (pe.getProjectStatus() != ProductEntity.ProjectStatus.DISTRIBUTION_READY) {
            throw new IllegalStateException("분배 준비 상태가 아님");
        }

        // 금액 확인
        if (pe.getDistributionAmount() == null ||
                pe.getDistributionAmount().compareTo(dto.getDistributionAmount()) != 0) {
            throw new IllegalStateException("입금 금액 불일치");
        }

        // 상태 전환
        pe.setProjectStatus(ProductEntity.ProjectStatus.DISTRIBUTING);
        pe.setDistributionSummary("창작자 분배금 입금 완료: " + dto.getDistributionAmount());
        pr.save(pe);
    }
}
