package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.common.security.JwtUtil;
import com.ddiring.BackEnd_Product.dto.admin.AdminApproveDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminRejectDto;
import com.ddiring.BackEnd_Product.dto.request.RequestDetailDto;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.service.AdminService;
import com.ddiring.BackEnd_Product.service.RequestService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/product/request")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService as;
    private final JwtUtil jwtUtil;

//    /** roles 헤더 파서 + ADMIN 권한 검증 */
//    private void requireAdmin(String rolesRaw) {
//        if (rolesRaw == null || rolesRaw.isBlank()) {
//            throw new ForbiddenException("권한 없음 (roles header missing)");
//        }
//
//        // ["ADMIN","CREATOR"] 같은 JSON 배열 문자열도 허용
//        String rolesCsv = rolesRaw.trim();
//        if (rolesCsv.startsWith("[") && rolesCsv.endsWith("]")) {
//            rolesCsv = rolesCsv.substring(1, rolesCsv.length() - 1)
//                    .replace("\"", "")
//                    .replace("'", "");
//        }
//
//        Set<String> have = Arrays.stream(rolesCsv.split("[,;\\s]+"))
//                .map(String::trim)
//                .filter(s -> !s.isEmpty())
//                .map(String::toUpperCase)
//                .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r)
//                .collect(Collectors.toSet());
//
//        if (!have.contains("ADMIN")) {
//            throw new ForbiddenException("권한 없음 (required=ADMIN, have=" + have + ")");
//        }
//    }
//
//    /** 요청 승인 (ADMIN 전용) */
//    @PostMapping("/approve")
//    public ResponseEntity<Void> approve(
//            @RequestBody AdminApproveDto dto,
//            @RequestHeader("userSeq") String userSeq,
//            @RequestHeader(value = "role") String roles) {
//
//        requireAdmin(roles);
//        as.approve(dto, userSeq.trim());
//        return ResponseEntity.ok().build();
//    }
//
//    /** 요청 거절 (ADMIN 전용) */
//    @PostMapping("/reject")
//    public ResponseEntity<Void> reject(
//            @RequestBody AdminRejectDto dto,
//            @RequestHeader("userSeq") String userSeq,
//            @RequestHeader(value = "role") String roles) {
//
//        requireAdmin(roles);
//        as.reject(dto, userSeq.trim());
//        return ResponseEntity.ok().build();
//    }

//    private String extractBearer(String authorizationHeader) {
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            throw new ForbiddenException("권한 없음 (토큰 누락)");
//        }
//        return authorizationHeader.substring(7).trim();
//    }
//
//    private String stripRolePrefix(String role) {
//        if (role == null) return null;
//        return role.startsWith("ROLE_") ? role.substring(5) : role;
//    }
//
//    /** 요청 승인 (ADMIN 전용) */
//    @PostMapping("/approve")
//    public ResponseEntity<Void> approve(
//            @RequestBody AdminApproveDto dto,
//            @RequestHeader("Authorization") String authorizationHeader) {
//
//        String token = extractBearer(authorizationHeader);
//        Claims claims = jwtUtil.parseClaims(token);
//
//        String role = stripRolePrefix(String.valueOf(claims.get("role")));
//        if (!"ADMIN".equalsIgnoreCase(role)) {
//            throw new ForbiddenException("권한 없음 (required=ADMIN, have=" + role + ")");
//        }
//
//        Object userSeqObj = claims.get("userSeq");
//        String userSeq = userSeqObj != null ? String.valueOf(userSeqObj).trim() : null;
//        if (userSeq == null || userSeq.isBlank()) {
//            throw new ForbiddenException("권한 없음 (userSeq claim 누락)");
//        }
//
//        as.approve(dto, userSeq); // ← String 그대로 전달
//        return ResponseEntity.ok().build();
//    }
//
//    /** 요청 거절 (ADMIN 전용) */
//    @PostMapping("/reject")
//    public ResponseEntity<Void> reject(
//            @RequestBody AdminRejectDto dto,
//            @RequestHeader("Authorization") String authorizationHeader) {
//
//        String token = extractBearer(authorizationHeader);
//        Claims claims = jwtUtil.parseClaims(token);
//
//        String role = stripRolePrefix(String.valueOf(claims.get("role")));
//        if (!"ADMIN".equalsIgnoreCase(role)) {
//            throw new ForbiddenException("권한 없음 (required=ADMIN, have=" + role + ")");
//        }
//
//        Object userSeqObj = claims.get("userSeq");
//        String userSeq = userSeqObj != null ? String.valueOf(userSeqObj).trim() : null;
//        if (userSeq == null || userSeq.isBlank()) {
//            throw new ForbiddenException("권한 없음 (userSeq claim 누락)");
//        }
//
//        as.reject(dto, userSeq); // ← String 그대로 전달
//        return ResponseEntity.ok().build();
//    }

    private String extractBearer(String header) {
        if (header == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Authorization 헤더 없음");
        }
        String[] parts = header.trim().split("\\s+", 2);
        if (parts.length != 2 || !parts[0].equalsIgnoreCase("Bearer")) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Authorization 형식 오류");
        }
        return parts[1].trim();
    }

    private String stripRolePrefix(String role) {
        if (role == null) return null;
        role = role.trim().toUpperCase();
        return role.startsWith("ROLE_") ? role.substring(5) : role;
    }

    private String extractRole(io.jsonwebtoken.Claims claims) {
        Object r = claims.get("role");
        if (r != null) return stripRolePrefix(String.valueOf(r));
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof java.util.List<?> list && !list.isEmpty()) {
            return stripRolePrefix(String.valueOf(list.get(0)));
        }
        return null;
    }

    @PostMapping("/approve")
    public ResponseEntity<Void> approve(
            @RequestBody AdminApproveDto dto,
            @RequestHeader("Authorization") String authorizationHeader) {

        final Claims claims;
        try {
            String token = extractBearer(authorizationHeader);
            claims = jwtUtil.parseClaims(token);
        } catch (JwtException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "유효하지 않은/만료된 토큰");
        }

        // 권한 체크
        String role = extractRole(claims);
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=ADMIN, have=" + role + ")");
        }

        // adminSeq 확인
        Object userSeqObj = claims.get("userSeq");
        if (userSeqObj == null || String.valueOf(userSeqObj).isBlank()) {
            throw new ForbiddenException("권한 없음 (userSeq claim 누락)");
        }
        String userSeq = String.valueOf(userSeqObj).trim();

        // 승인 처리
        as.approve(dto, userSeq);

        return ResponseEntity.ok().build();
    }
}