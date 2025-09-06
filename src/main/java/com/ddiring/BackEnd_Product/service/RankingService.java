package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final ProductRepository pr;

    /* ---------- 전체 조회수 랭킹 ---------- */
    public List<ProductEntity> getTopByViewCount() {
        return pr.findTop10ByOrderByViewCountDesc();
    }

    /* ---------- 투자 진행중 조회수 랭킹 ---------- */
    public List<ProductEntity> getTopByViewCountWithDeadline() {
        return pr.findTop10ByEndDateAfterOrderByViewCountDesc(LocalDateTime.now());
    }

    /* ---------- 모금액 랭킹 ---------- */
    public List<ProductEntity> getTopByAmount() {
        return pr.findTop10ByOrderByAmountDesc();
    }
}
