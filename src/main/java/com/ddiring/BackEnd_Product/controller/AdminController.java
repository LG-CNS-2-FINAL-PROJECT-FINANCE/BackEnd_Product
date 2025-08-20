package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.common.security.JwtUtil;
import com.ddiring.BackEnd_Product.dto.admin.AdminApproveDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminRejectDto;
import com.ddiring.BackEnd_Product.service.AdminService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/request")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService as;
    private final JwtUtil jwtUtil;

    private String extractBearer(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            throw new ForbiddenException("권한 없음 (토큰 누락)");
        }
        return authorizationHeader.substring(7).trim();
    }

    private String stripRolePrefix(String role) {
        if (role == null) return null;
        return role.startsWith("ROLE_") ? role.substring(5) : role;
    }

    /** 요청 승인 (ADMIN 전용) */
    @PostMapping("/approve")
    public ResponseEntity<Void> approve(
            @RequestBody AdminApproveDto dto,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = extractBearer(authorizationHeader);
        Claims claims = jwtUtil.parseClaims(token);

        String role = stripRolePrefix(String.valueOf(claims.get("role")));
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=ADMIN, have=" + role + ")");
        }

        Object userSeqObj = claims.get("userSeq");
        String userSeq = userSeqObj != null ? String.valueOf(userSeqObj).trim() : null;
        if (userSeq == null || userSeq.isBlank()) {
            throw new ForbiddenException("권한 없음 (userSeq claim 누락)");
        }

        as.approve(dto, userSeq); // ← String 그대로 전달
        return ResponseEntity.ok().build();
    }

    /** 요청 거절 (ADMIN 전용) */
    @PostMapping("/reject")
    public ResponseEntity<Void> reject(
            @RequestBody AdminRejectDto dto,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = extractBearer(authorizationHeader);
        Claims claims = jwtUtil.parseClaims(token);

        String role = stripRolePrefix(String.valueOf(claims.get("role")));
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=ADMIN, have=" + role + ")");
        }

        Object userSeqObj = claims.get("userSeq");
        String userSeq = userSeqObj != null ? String.valueOf(userSeqObj).trim() : null;
        if (userSeq == null || userSeq.isBlank()) {
            throw new ForbiddenException("권한 없음 (userSeq claim 누락)");
        }

        as.reject(dto, userSeq); // ← String 그대로 전달
        return ResponseEntity.ok().build();
    }
}