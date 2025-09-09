package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.kafka.NotificationProducer;
import com.ddiring.BackEnd_Product.kafka.enums.NotificationType;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeadlineUpdateService {

    private final ProductRepository pr;
    private final NotificationProducer notificationProducer;

    // 매일 자정마다 실행 >> postman 기준 자정 맞출라면 utc 기준시에 추가한 시간으로 계산
    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void updateDeadlines() {
        log.info("[테스트] D-Day 스케줄러 실행됨");

        List<ProductEntity> products = pr.findAll();

        for (ProductEntity product : products) {
            int dDay = product.dDay();
            product.setDeadline(dDay);

            // 🔔 사전 알림 (7일, 3일, 1일 전)
            if (dDay == 7 || dDay == 3 || dDay == 1) {
                // 창작자 알림
                notificationProducer.sendNotification(
                        List.of(product.getUserSeq()),
                        NotificationType.INFORMATION.name(),
                        "마감 임박",
                        "상품(" + product.getTitle() + ")의 마감일이 " + dDay + "일 남았습니다."
                );

                // 즐겨찾기 알림
                notificationProducer.sendNotification(
                        new ArrayList<>(product.getFavorites()),
                        NotificationType.INFORMATION.name(),
                        "마감 임박",
                        "상품(" + product.getTitle() + ")의 마감일이 " + dDay + "일 남았습니다."
                );

                log.info("프로젝트 {} 마감 {}일 전 알림 발송 완료", product.getProjectId(), dDay);
            }

            // 🔔 마감된 경우 (투자 종료 처리)
            if (dDay < 0 && product.getProjectStatus() != ProductEntity.ProjectStatus.TRADING) {
                product.setProjectStatus(ProductEntity.ProjectStatus.TRADING);

                // 마감일 도래로 인한 투자활성화 알림(창작자)
                notificationProducer.sendNotification(
                        List.of(product.getUserSeq()),
                        NotificationType.INFORMATION.name(),
                        "상품 투자 종료",
                        "상품(" + product.getTitle() + ")의 투자 기간이 종료되어 2차 거래를 시작합니다."
                );

                // 마감일 도래로 인한 투자활성화 알림(즐겨찾기)
                notificationProducer.sendNotification(
                        new ArrayList<>(product.getFavorites()),
                        NotificationType.INFORMATION.name(),
                        "상품 투자 종료",
                        "상품(" + product.getTitle() + ")의 투자 기간이 종료되어 2차 거래를 시작합니다."
                );

                log.info("프로젝트 {} 마감됨 → 상태 TRADING(2차거래)로 변경", product.getProjectId());
            }
        }

        pr.saveAll(products);
        log.info("모든 상품의 D-Day(deadline) 갱신 완료");
    }
}