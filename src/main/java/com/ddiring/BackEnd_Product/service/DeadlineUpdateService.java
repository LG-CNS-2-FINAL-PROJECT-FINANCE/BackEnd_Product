package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeadlineUpdateService {

    private final ProductRepository pr;

    // 매일 자정마다 실행 >> postman 기준 자정 맞출라면 utc 기준시에 추가한 시간으로 계산
    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void updateDeadlines() {
        log.info("[테스트] D-Day 스케줄러 실행됨");

        List<ProductEntity> products = pr.findAll();

        for (ProductEntity product : products) {
            int dDay = product.dDay();
            product.setDeadline(dDay);

            // 마감된 경우 상태 변경
            if (dDay < 0 && product.getProjectStatus() != ProductEntity.ProjectStatus.TRADING) {
                product.setProjectStatus(ProductEntity.ProjectStatus.TRADING);
                log.info("프로젝트 {} 마감됨 → 상태 TRADING(2차거래)로 변경", product.getProjectId());
            }
        }

        pr.saveAll(products);
        log.info("모든 상품의 D-Day(deadline) 갱신 완료");
    }
}