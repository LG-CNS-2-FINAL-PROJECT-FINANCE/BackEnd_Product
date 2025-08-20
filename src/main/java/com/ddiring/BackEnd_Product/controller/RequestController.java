package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.security.JwtAuthGuard;
import com.ddiring.BackEnd_Product.dto.request.RequestDetailDto;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.service.RequestService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product/request")
public class RequestController {

    private final RequestService rs;
    private final JwtAuthGuard guard;

    @GetMapping
    public ResponseEntity<List<RequestListDto>> getAllRequest(
            @RequestHeader("Authorization") String auth) {

        Claims c = guard.requireClaims(auth);
        guard.requireAnyRole(c, "ADMIN");

        List<RequestListDto> requestList = rs.getAllRequest();
        return ResponseEntity.ok(requestList);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<RequestDetailDto> getRequest(
            @PathVariable String requestId,
            @RequestHeader("Authorization") String auth) {

        Claims c = guard.requireClaims(auth);
        guard.requireAnyRole(c, "ADMIN");

        RequestDetailDto rdd = rs.getRequestByRequestId(requestId);
        return ResponseEntity.ok(rdd);
    }
}
