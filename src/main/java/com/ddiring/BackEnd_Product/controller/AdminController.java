package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.dto.AdminApproveDto;
import com.ddiring.BackEnd_Product.dto.AdminRejectDto;
import com.ddiring.BackEnd_Product.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/requests")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    /** 요청 승인 */
    @PostMapping("/approve")
    public ResponseEntity<Void> approve(
            @RequestBody AdminApproveDto dto,
            @RequestHeader(value = "X-User-Id", defaultValue = "100") int adminSeq) {

        adminService.approve(dto, adminSeq);
        return ResponseEntity.ok().build();
    }

    /** 요청 거절 */
    @PostMapping("/reject")
    public ResponseEntity<Void> reject(
            @RequestBody AdminRejectDto dto,
            @RequestHeader(value = "X-User-Id", defaultValue = "100") int adminSeq) {

        adminService.reject(dto, adminSeq);
        return ResponseEntity.ok().build();
    }
}