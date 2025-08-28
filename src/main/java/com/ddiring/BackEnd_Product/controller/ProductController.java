package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.util.GatewayRequestHeaderUtils;
import com.ddiring.BackEnd_Product.dto.product.ProductDetailDto;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService ps;

    @GetMapping
    public ResponseEntity<List<ProductListDto>> getAllProducts() {
        List<ProductListDto> productList = ps.getAllProject();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProductDetailDto> getProduct(@PathVariable String projectId) {
        String userSeq = GatewayRequestHeaderUtils.getUserSeqSafe();
        ProductDetailDto pdd = ps.getProductByProjectId(projectId, userSeq);
        return ResponseEntity.ok(pdd);
    }
}