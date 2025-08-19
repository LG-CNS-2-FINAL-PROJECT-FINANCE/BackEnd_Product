package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.dto.admin.AdminApproveDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminRejectDto;
import com.ddiring.BackEnd_Product.dto.request.RequestDetailDto;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.service.AdminService;
import com.ddiring.BackEnd_Product.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product/request")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService as;

//    /** 요청 승인 */
//    @PostMapping("/approve")
//    public ResponseEntity<Void> approve(
//            @RequestBody AdminApproveDto dto,
//            @RequestHeader(value = "X-User-Id", defaultValue = "100") int adminId) {
//
//        as.approve(dto, adminSeq);
//        return ResponseEntity.ok().build();
//    }
//
//    /** 요청 거절 */
//    @PostMapping("/reject")
//    public ResponseEntity<Void> reject(
//            @RequestBody AdminRejectDto dto,
//            @RequestHeader(value = "X-User-Id", defaultValue = "100") int adminId) {
//
//        as.reject(dto, adminSeq);
//        return ResponseEntity.ok().build();
//    }

    /** roles 헤더 파서 + ADMIN 권한 검증 */
    private void requireAdmin(String rolesRaw) {
        if (rolesRaw == null || rolesRaw.isBlank()) {
            throw new ForbiddenException("권한 없음 (roles header missing)");
        }

        // ["ADMIN","CREATOR"] 같은 JSON 배열 문자열도 허용
        String rolesCsv = rolesRaw.trim();
        if (rolesCsv.startsWith("[") && rolesCsv.endsWith("]")) {
            rolesCsv = rolesCsv.substring(1, rolesCsv.length() - 1)
                    .replace("\"", "")
                    .replace("'", "");
        }

        Set<String> have = Arrays.stream(rolesCsv.split("[,;\\s]+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase)
                .map(r -> r.startsWith("ROLE_") ? r.substring(5) : r)
                .collect(Collectors.toSet());

        if (!have.contains("ADMIN")) {
            throw new ForbiddenException("권한 없음 (required=ADMIN, have=" + have + ")");
        }
    }

//    /** "userSeq" → int 파싱 (AdminService가 int를 받는 현재 시그니처 유지) */
//    private int parseAdminId(String adminId) {
//        try {
//            return String.parseInt(adminId.trim());
//        } catch (NumberFormatException e) {
//            throw new IllegalStateException("잘못된 관리자 ID 형식");
//        }
//    }

    /** 요청 승인 (ADMIN 전용) */
    @PostMapping("/approve")
    public ResponseEntity<Void> approve(
            @RequestBody AdminApproveDto dto,
            @RequestHeader("adminId") String adminId,
            @RequestHeader(value = "role") String roles) {

        requireAdmin(roles);
        as.approve(dto, adminId.trim());
        return ResponseEntity.ok().build();
    }

    /** 요청 거절 (ADMIN 전용) */
    @PostMapping("/reject")
    public ResponseEntity<Void> reject(
            @RequestBody AdminRejectDto dto,
            @RequestHeader("adminId") String adminId,
            @RequestHeader(value = "role") String roles) {

        requireAdmin(roles);
        as.reject(dto, adminId.trim());
        return ResponseEntity.ok().build();
    }
}