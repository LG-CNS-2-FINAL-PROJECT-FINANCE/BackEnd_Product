package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.security.AuthUtils;
import com.ddiring.BackEnd_Product.dto.admin.AdminApproveDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminClosedDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminHoldDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminRejectDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.service.AdminService;
import com.ddiring.BackEnd_Product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService as;
    private final ProductService ps;

    @PostMapping("/request/approve")
    public ResponseEntity<Void> approve(@RequestBody @Valid AdminApproveDto dto) {
        String adminSeq = AuthUtils.requireAdmin();
        as.approve(dto, adminSeq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/request/reject")
    public ResponseEntity<Void> reject(@RequestBody @Valid AdminRejectDto dto) {
        String adminSeq = AuthUtils.requireAdmin();
        as.reject(dto, adminSeq);
        return ResponseEntity.ok().build();
    }

    /** 게시물 HOLD 토글 (ADMIN 권한 필요) */
    @PostMapping("/hold/toggle/{projectId}")
    public ResponseEntity<AdminHoldDto> toggleHold(
            @PathVariable("projectId") String projectId,
            @RequestBody @Valid AdminHoldDto dto) {
        String adminSeq = AuthUtils.requireAdmin();

        ProductEntity.ProjectVisibility newStatus =
                as.toggleVisibility(projectId, dto.getHoldReason(), adminSeq);

        AdminHoldDto response = new AdminHoldDto(
                projectId,
                newStatus.name()
        );

        return ResponseEntity.ok(response);
    }

    /** 게시물 상태 CLOSED 변경 (ADMIN 권한 필요) */
    @PostMapping("/closed/{projectId}")
    public ResponseEntity<AdminClosedDto> closeProduct(@PathVariable("projectId") String projectId) {
        String adminSeq = AuthUtils.requireAdmin();

        ProductEntity updated = ps.closedProduct(projectId, adminSeq);

        AdminClosedDto response = new AdminClosedDto(
                updated.getProjectId(),
                updated.getProjectStatus().name(),
                updated.getReason()
        );

        return ResponseEntity.ok(response);
    }
}