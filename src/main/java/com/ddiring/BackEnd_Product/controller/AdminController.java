package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.dto.admin.AdminApproveDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminRejectDto;
import com.ddiring.BackEnd_Product.dto.request.RequestDetailDto;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.service.AdminService;
import com.ddiring.BackEnd_Product.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/requests")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService as;
    private final RequestService rs;

    /** 요청 승인 */
    @PostMapping("/approve")
    public ResponseEntity<Void> approve(
            @RequestBody AdminApproveDto dto,
            @RequestHeader(value = "X-User-Id", defaultValue = "100") int adminSeq) {

        as.approve(dto, adminSeq);
        return ResponseEntity.ok().build();
    }

    /** 요청 거절 */
    @PostMapping("/reject")
    public ResponseEntity<Void> reject(
            @RequestBody AdminRejectDto dto,
            @RequestHeader(value = "X-User-Id", defaultValue = "100") int adminSeq) {

        as.reject(dto, adminSeq);
        return ResponseEntity.ok().build();
    }
}