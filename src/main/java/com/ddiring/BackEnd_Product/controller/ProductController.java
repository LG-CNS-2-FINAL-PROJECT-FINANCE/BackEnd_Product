package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.exception.CustomException;
import com.ddiring.BackEnd_Product.common.security.JwtAuthGuard;
import com.ddiring.BackEnd_Product.common.util.GatewayRequestHeaderUtils;
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

    @GetMapping
    public ResponseEntity<List<ProductListDto>> getAllProducts() {
        List<ProductListDto> productList = ps.getAllProject();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProductDetailDto> getProduct(@PathVariable String projectId) {
        String userSeq = null;
        try {
            userSeq = GatewayRequestHeaderUtils.getUserSeq();
        } catch (CustomException ex) {
            // 헤더 없으면 그냥 null로 둠 (비로그인 사용자)
        }
        ProductDetailDto pdd = ps.getProductByProjectId(projectId, userSeq);
        return ResponseEntity.ok(pdd);
    }
}