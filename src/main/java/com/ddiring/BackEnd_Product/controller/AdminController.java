package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.common.security.JwtAuthGuard;
import com.ddiring.BackEnd_Product.common.security.JwtUtil;
import com.ddiring.BackEnd_Product.dto.admin.AdminApproveDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminRejectDto;
import com.ddiring.BackEnd_Product.service.AdminService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
