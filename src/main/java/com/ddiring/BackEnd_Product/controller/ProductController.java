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

    @GetMapping("/open")
    public ResponseEntity<List<ProductListDto>> getAllOpenProject() {
        List<ProductListDto> productList = ps.getAllOpenProject();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/unOpen")
    public ResponseEntity<List<ProductListDto>> getAllUnOpenProject() {
        List<ProductListDto> productList = ps.getAllUnOpenProject();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProductDetailDto> getProject(@PathVariable String projectId) {
        String userSeq = GatewayRequestHeaderUtils.getUserSeqSafe(); // 로그인 안 하면 null
        ProductDetailDto pdd = ps.getProjectId(projectId, userSeq);
        return ResponseEntity.ok(pdd);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ProductListDto>> getAllProjectAdmin() {
        AuthUtils.requireAdmin();
        List<ProductListDto> productList = ps.getAllProjectAdmin();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/admin/{projectId}")
    public ResponseEntity<ProductDetailDto> getProjectIdAdmin(@PathVariable String projectId) {
        String adminSeq = AuthUtils.requireAdmin();
        ProductDetailDto pdd = ps.getProjectIdAdmin(projectId, adminSeq);
        return ResponseEntity.ok(pdd);
    }
}