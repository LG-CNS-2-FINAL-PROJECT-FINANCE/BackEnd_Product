package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.dto.creator.CreatorUpdateDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorCreateDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorStopDto;
import com.ddiring.BackEnd_Product.service.CreatorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/product/request")
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorService cs;

    /** 공통: roles 헤더에 특정 역할이 있는지 확인 */
    private void requireRole(String rolesCsv, String... required) {
        if (rolesCsv == null) throw new ForbiddenException("권한 없음");
        var set = java.util.Arrays.stream(rolesCsv.split(","))
                .map(String::trim).filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toSet());
        for (String r : required) if (set.contains(r)) return;
        throw new ForbiddenException("권한 없음");
    }

    /** 프로젝트 신규 등록: CREATOR 권한 필요 */
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> create(
            @RequestBody CreatorCreateDto dto,
            @RequestHeader("userSeq") String userSeq,
            @RequestHeader(value = "role") String role) {

        requireRole(role, "CREATOR");
        String requestId = cs.create(dto, userSeq.trim());
        return ResponseEntity.ok(Map.of("requestId", requestId));
    }

    /** 프로젝트 정보 수정: CREATOR 필요(본인 소유 검증은 서비스에서 추가 권장) */
    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> update(
            @RequestBody CreatorUpdateDto dto,
            @RequestHeader("userSeq") String userSeq,
            @RequestHeader(value = "role") String role) {

        requireRole(role, "CREATOR");
        String requestId = cs.update(dto, userSeq.trim());
        return ResponseEntity.ok(Map.of("requestId", requestId));
    }

    /** 프로젝트 중단 요청: CREATOR 또는 ADMIN */
    @PostMapping("/stop")
    public ResponseEntity<Map<String, String>> stop(
            @RequestBody CreatorStopDto dto,
            @RequestHeader("userSeq") String userSeq,
            @RequestHeader(value = "role") String role) {

        requireRole(role, "CREATOR");
        String requestId = cs.stop(dto, userSeq.trim());
        return ResponseEntity.ok(Map.of("requestId", requestId));
    }
}
