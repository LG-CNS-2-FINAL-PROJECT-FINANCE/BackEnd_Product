package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.common.security.JwtAuthGuard;
import com.ddiring.BackEnd_Product.common.util.GatewayRequestHeaderUtils;
import com.ddiring.BackEnd_Product.dto.request.RequestDetailDto;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.service.RequestService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product/request")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService rs;

    @GetMapping("/admin")
    public ResponseEntity<List<RequestListDto>> getAdminAllRequest() {
        String role = GatewayRequestHeaderUtils.getRole();

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=ADMIN)");
        }

        List<RequestListDto> requestList = rs.getAllRequest();
        return ResponseEntity.ok(requestList);
    }

    @GetMapping("/admin/{requestId}")
    public ResponseEntity<RequestDetailDto> getAdminRequest(@PathVariable String requestId) {
        String role = GatewayRequestHeaderUtils.getRole();

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=ADMIN)");
        }

        RequestDetailDto rdd = rs.getRequestByRequestId(requestId);
        return ResponseEntity.ok(rdd);
    }
}
