package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.security.AuthUtils;
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
        String userSeq = GatewayRequestHeaderUtils.getUserSeqSafe(); // 로그인 안 하면 null
        ProductDetailDto pdd = ps.getProductByProjectId(projectId, userSeq);
        return ResponseEntity.ok(pdd);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ProductListDto>> getAdminAllProducts() {
        AuthUtils.requireAdmin();
        List<ProductListDto> productList = ps.getAllProjectAdmin();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/admin/{projectId}")
    public ResponseEntity<ProductDetailDto> getAdminProduct(@PathVariable String projectId) {
        String adminSeq = AuthUtils.requireAdmin();
        ProductDetailDto pdd = ps.getProductByProjectIdAdmin(projectId, adminSeq);
        return ResponseEntity.ok(pdd);
    }
}