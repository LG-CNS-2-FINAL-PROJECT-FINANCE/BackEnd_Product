package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product/market")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService ms;

    @GetMapping("/end")
    public Page<ProductListDto> getEndedProducts (Pageable p) {
        List<ProductListDto> content = ms.getEndedProducts(p);
        long total = content.size(); // 필요 시 count 쿼리 별도 호출

        return new PageImpl<>(content, p, total);
    }
}
