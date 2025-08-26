package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.common.util.GatewayRequestHeaderUtils;
import com.ddiring.BackEnd_Product.dto.creator.CreatorCreateDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorStopDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorUpdateDto;
import com.ddiring.BackEnd_Product.service.CreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/product/request")
@RequiredArgsConstructor
public class CreatorController {

    private final CreatorService cs;

    @PostMapping("/create")
    public ResponseEntity<Map<String,String>> create(@RequestBody @Valid CreatorCreateDto dto) {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();
        String role = GatewayRequestHeaderUtils.getRole();

        if (!"CREATOR".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=CREATOR)");
        }

        return ResponseEntity.ok(Map.of("requestId", cs.create(dto, userSeq)));
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String,String>> update(@RequestBody @Valid CreatorUpdateDto dto) {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();
        String role = GatewayRequestHeaderUtils.getRole();

        if (!"CREATOR".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=CREATOR)");
        }

        return ResponseEntity.ok(Map.of("requestId", cs.update(dto, userSeq)));
    }

    @PostMapping("/stop")
    public ResponseEntity<Map<String,String>> stop(@RequestBody @Valid CreatorStopDto dto) {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();
        String role = GatewayRequestHeaderUtils.getRole();

        if (!"CREATOR".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=CREATOR)");
        }

        return ResponseEntity.ok(Map.of("requestId", cs.stop(dto, userSeq)));
    }
}