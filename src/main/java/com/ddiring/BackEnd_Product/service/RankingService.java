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

    public List<ProductEntity> getTopByViewCount() {
        return pr.findTop10ByOrderByViewCountDesc();
    }

    public List<ProductEntity> getTopByViewCountWithDeadline() {
        return pr.findTop10ByEndDateAfterOrderByViewCountDesc(LocalDateTime.now());
    }

    public List<ProductEntity> getTopByAmount() {
        return pr.findTop10ByOrderByAmountDesc();
    }
}
