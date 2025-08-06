package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.dto.ProductDetailDto;
import com.ddiring.BackEnd_Product.dto.ProductListDto;
import com.ddiring.BackEnd_Product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService ps;

    @GetMapping
    public ResponseEntity<List<ProductListDto>> getAllProducts() {
        List<ProductListDto> productList = ps.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailDto> getProduct(@PathVariable String productId) {
        ProductDetailDto pdd = ps.getProductById(productId);
        return ResponseEntity.ok(pdd);


//    @GetMapping("/view/{productId}")
//    public ResponseEntity<ProductDto> view(@PathVariable String productId) {
//        // 조회수 증가
//        vs.viewCount(productId);
//
//        // 최신 viewCount 조회
//        ProductEntity product = pr.findById(productId)
//                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));
//
//        // DTO로 변환하여 응답
//        ProductDto productDto = ProductDto.from(product);
//        return ResponseEntity.ok(productDto);
//    }
    }
}
