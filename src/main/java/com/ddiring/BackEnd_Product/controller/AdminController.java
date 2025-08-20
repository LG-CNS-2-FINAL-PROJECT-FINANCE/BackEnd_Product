package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.security.JwtAuthGuard;
import com.ddiring.BackEnd_Product.dto.admin.AdminApproveDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminHoldDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminRejectDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.service.AdminService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/product/request")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService as;
    private final JwtAuthGuard guard;

    @PostMapping("/approve")
    public ResponseEntity<Void> approve(@RequestHeader("Authorization") String auth,
                                        @RequestBody @Valid AdminApproveDto dto) {
        Claims c = guard.requireClaims(auth);
        guard.requireAnyRole(c, "ADMIN");
        String adminSeq = guard.requireUserSeq(c); // 필요 시 Integer.parseInt
        as.approve(dto, adminSeq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> reject(@RequestHeader("Authorization") String auth,
                                       @RequestBody @Valid AdminRejectDto dto) {
        Claims c = guard.requireClaims(auth);
        guard.requireAnyRole(c, "ADMIN");
        String adminSeq = guard.requireUserSeq(c);
        as.reject(dto, adminSeq);
        return ResponseEntity.ok().build();
    }

    /** 게시물 HOLD 토글 (ADMIN 권한 필요) */
    @PostMapping("/hold/toggle/{id}")
    public ResponseEntity<Map<String, Object>> toggleHold(@PathVariable("id") String productId,
                                                          @RequestHeader("Authorization") String auth,
                                                          @RequestBody @Valid AdminHoldDto dto) {

        Claims c = guard.requireClaims(auth);
        guard.requireAnyRole(c, "ADMIN");

        String adminSeq = guard.requireUserSeq(c); // 클라이언트가 보내는 adminId는 신뢰 X

        ProductEntity.ProductStatus newStatus = as.toggleHold(productId, dto.getHoldReason(), adminSeq);
        boolean nowHold = (newStatus == ProductEntity.ProductStatus.HOLD);

        return ResponseEntity.ok(Map.of(
                "productId", productId,
                "status", newStatus.name(),
                "hold", nowHold
        ));
    }
}
