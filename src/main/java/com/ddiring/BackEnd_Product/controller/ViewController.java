package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.dto.ViewDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import com.ddiring.BackEnd_Product.service.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/view")
@RequiredArgsConstructor
public class ViewController {

    private final ViewService vs;
    private final ProductRepository pr;

    @GetMapping("/{productId}")
    public ResponseEntity<ViewDto> view(@PathVariable String productId) {
        // 조회수 증가
        vs.viewCount(productId);

        // 최신 viewCount 조회
        ProductEntity product = pr.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));

        // DTO로 변환하여 응답
        ViewDto viewDto = ViewDto.from(product);
        return ResponseEntity.ok(viewDto);
    }
}
