package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rs;

    @GetMapping("/view")
    public List<ProductEntity> getTopByViewCount() {
        return rs.getTopByViewCount();
    }

    @GetMapping("/view/deadline")
    public List<ProductEntity> getTopByViewCountWithDeadline() {
        return rs.getTopByViewCountWithDeadline();
    }

    @GetMapping("/amount")
    public List<ProductEntity> getTopByAmount() {
        return rs.getTopByAmount();
    }
}
