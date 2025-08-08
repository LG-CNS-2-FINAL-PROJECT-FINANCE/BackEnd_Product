package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.dto.product.ProductDetailDto;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService ps;

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
