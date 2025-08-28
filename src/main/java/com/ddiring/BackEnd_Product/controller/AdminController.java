package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.common.util.GatewayRequestHeaderUtils;
import com.ddiring.BackEnd_Product.dto.admin.AdminApproveDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminHoldDto;
import com.ddiring.BackEnd_Product.dto.admin.AdminRejectDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.service.AdminService;
import com.ddiring.BackEnd_Product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService as;
    private final ProductService ps;

    @PostMapping("/request/approve")
    public ResponseEntity<Void> approve(@RequestBody @Valid AdminApproveDto dto) {
        String adminSeq = GatewayRequestHeaderUtils.getUserSeq();
        String role = GatewayRequestHeaderUtils.getRole();

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=ADMIN)");
        }

        as.approve(dto, adminSeq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/request/reject")
    public ResponseEntity<Void> reject(@RequestBody @Valid AdminRejectDto dto) {
        String adminSeq = GatewayRequestHeaderUtils.getUserSeq();
        String role = GatewayRequestHeaderUtils.getRole();

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=ADMIN)");
        }

        as.reject(dto, adminSeq);
        return ResponseEntity.ok().build();
    }

    /** 게시물 HOLD 토글 (ADMIN 권한 필요) */
    @PostMapping("/hold/toggle/{projectId}")
    public ResponseEntity<Map<String, Object>> toggleHold(@PathVariable("projectId") String projectId,
                                                          @RequestBody @Valid AdminHoldDto dto) {
        String adminSeq = GatewayRequestHeaderUtils.getUserSeq();
        String role = GatewayRequestHeaderUtils.getRole();

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=ADMIN)");
        }

        ProductEntity.ProductStatus newStatus = as.toggleHold(projectId, dto.getHoldReason(), adminSeq);
        boolean nowHold = (newStatus == ProductEntity.ProductStatus.HOLD);

        return ResponseEntity.ok(Map.of(
                "product", projectId,
                "status", newStatus.name(),
                "hold", nowHold
        ));
    }

    @PostMapping("/closed/{projectId}")
    public ResponseEntity<Map<String, Object>> closeProduct(@PathVariable("projectId") String projectId) {

        String adminSeq = GatewayRequestHeaderUtils.getUserSeq();
        String role = GatewayRequestHeaderUtils.getRole();

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=ADMIN)");
        }

        ProductEntity updated = ps.closedProduct(projectId, adminSeq);

        return ResponseEntity.ok(Map.of(
                "projectId", updated.getProjectId(),
                "status", updated.getStatus().name(),
                "reason", updated.getReason()
        ));
    }
}