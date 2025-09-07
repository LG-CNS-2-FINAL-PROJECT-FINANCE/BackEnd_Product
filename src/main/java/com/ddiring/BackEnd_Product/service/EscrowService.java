package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.escrow.AmountDto;
import com.ddiring.BackEnd_Product.dto.escrow.EscrowDistributionDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.kafka.NotificationProducer;
import com.ddiring.BackEnd_Product.kafka.enums.NotificationType;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EscrowService {

    private final ProductRepository pr;
    private final NotificationProducer notificationProducer;

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

            // 🔔 상태 변경: 100% 달성 시 FUNDING_LOCKED
            if (percent.compareTo(new BigDecimal("100")) >= 0
                    && pe.getProjectStatus() == ProductEntity.ProjectStatus.OPEN) {
                pe.setProjectStatus(ProductEntity.ProjectStatus.FUNDING_LOCKED);

                // 모금액 달성으로 인한 조기마감 알림(창작자)
                notificationProducer.sendNotification(
                        List.of(pe.getUserSeq()),
                        NotificationType.INFORMATION.name(),
                        "목표금액 달성",
                        "상품(" + pe.getTitle() + ")의 목표금액이 달성되어 조기 마감되었습니다."
                );

                // 모금액 달성으로 인한 조기마감 알림(즐겨찾기)
                notificationProducer.sendNotification(
                        new ArrayList<>(pe.getFavorites()),
                        NotificationType.INFORMATION.name(),
                        "목표금액 달성",
                        "상품(" + pe.getTitle() + ")의 목표금액이 달성되어 조기 마감되었습니다."
                );

            } else if (percent.compareTo(new BigDecimal("100")) < 0
                    && pe.getProjectStatus() == ProductEntity.ProjectStatus.FUNDING_LOCKED) {

                // 환불 등으로 목표치 미만이 되면 다시 OPEN
                pe.setProjectStatus(ProductEntity.ProjectStatus.OPEN);

                // 모금액 감소 알림(창작자)
                notificationProducer.sendNotification(
                        List.of(pe.getUserSeq()),
                        NotificationType.INFORMATION.name(),
                        "목표금액 달성 해제",
                        "상품(" + pe.getTitle() + ")의 목표금액 달성이 해제되어 다시 모집 중 상태로 변경되었습니다."
                );

                // 모금액 감소 알림(즐겨찾기)
                notificationProducer.sendNotification(
                        new ArrayList<>(pe.getFavorites()),
                        NotificationType.INFORMATION.name(),
                        "목표금액 달성 해제",
                        "상품(" + pe.getTitle() + ")의 목표금액 달성이 해제되어 다시 모집 중 상태로 변경되었습니다."
                );
            }

            pr.save(pe);
        }
    }

    /* ---------- escrow 분배금 입금 확인 ---------- */
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

        // 🔔 분배금 입금 확인(창작자)
        notificationProducer.sendNotification(
                List.of(pe.getUserSeq()),
                NotificationType.INFORMATION.name(),
                "분배금 입금 확인",
                "상품(" + pe.getTitle() + ")의 분배금 입금이 확인되었습니다."
        );
    }
}
