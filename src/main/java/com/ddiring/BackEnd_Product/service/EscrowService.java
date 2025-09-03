package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.escrow.AmountDto;
import com.ddiring.BackEnd_Product.dto.escrow.EscrowDistributionDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class EscrowService {

    private final ProductRepository pr;

    /* ---------- 모금액 저장 및 달성률 계산 ---------- */
    @Transactional
    public void receiveAmount(AmountDto dto) {
        ProductEntity pe = pr.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));

        BigDecimal newAmount = dto.getBalance();
        if (newAmount == null) return;

        // 금액 변경 시에만 반영
        if (pe.getAmount() == null || pe.getAmount().compareTo(newAmount) != 0) {
            pe.setAmount(newAmount);

            // 달성률 계산
            BigDecimal goal = pe.getGoalAmount();
            BigDecimal percent = BigDecimal.ZERO;
            if (goal != null && goal.compareTo(BigDecimal.ZERO) > 0) {
                percent = newAmount
                        .divide(goal, 4, RoundingMode.HALF_UP)  // 소수 넉넉히 계산
                        .multiply(new BigDecimal("100"))
                        .min(new BigDecimal("100"));            // 100% 이상 방지
            }
            pe.setPercent(percent.setScale(1, RoundingMode.HALF_UP));

            // 상태 변경: 100% 달성 시 END
            if (percent.compareTo(new BigDecimal("100")) >= 0
                    && pe.getProjectStatus() == ProductEntity.ProjectStatus.OPEN) {
                pe.setProjectStatus(ProductEntity.ProjectStatus.FUNDING_LOCKED);
            }

            pr.save(pe);
        }
    }

    /* ---------- escrow 모금액 입금 확인 ---------- */
    @Transactional
    public void sendDistribution(EscrowDistributionDto dto) {
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
        pr.save(pe);
    }
}
