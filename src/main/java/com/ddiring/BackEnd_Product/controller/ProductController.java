package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.security.JwtAuthGuard;
import com.ddiring.BackEnd_Product.dto.product.ProductDetailDto;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.service.ProductService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService ps;
//    private final JwtAuthGuard guard; // JWT 인증/권한 가드

//    /** 목록 조회: 로그인 필수 (ADMIN/CREATOR/USER 중 하나) */
//    @GetMapping
//    public ResponseEntity<List<ProductListDto>> getAllProducts(
//            @RequestHeader("Authorization") String auth) {
//
//        Claims c = guard.requireClaims(auth);
//        guard.requireAnyRole(c, "ADMIN", "CREATOR", "USER"); // 비로그인/무권한 차단
//
//        List<ProductListDto> productList = ps.getAllProduct();
//        return ResponseEntity.ok(productList);
//    }
//
//    /** 상세 조회: 로그인 필수 (ADMIN/CREATOR/USER 중 하나) */
//    @GetMapping("/{productId}")
//    public ResponseEntity<ProductDetailDto> getProduct(
//            @PathVariable String productId,
//            @RequestHeader("Authorization") String auth) {
//
//        Claims c = guard.requireClaims(auth);
//        guard.requireAnyRole(c, "ADMIN", "CREATOR", "USER"); // 비로그인/무권한 차단
//
//        ProductDetailDto pdd = ps.getProductByProductId(productId);
//        return ResponseEntity.ok(pdd);
//    }

    @GetMapping
    public ResponseEntity<List<ProductListDto>> getAllProducts() {
        List<ProductListDto> productList = ps.getAllProduct();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailDto> getProduct(@PathVariable String productId) {
        ProductDetailDto pdd = ps.getProductByProductId(productId);
        return ResponseEntity.ok(pdd);
    }
}