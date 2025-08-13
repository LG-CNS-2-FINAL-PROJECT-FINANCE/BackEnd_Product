package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.dto.creator.CreatorUpdateDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorCreateDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorStopDto;
import com.ddiring.BackEnd_Product.service.CreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/creator/requests")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('CREATOR')")
public class CreatorController {
    private final CreatorService cs;

    /** 프로젝트 신규 등록 */
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> create(
            @RequestBody CreatorCreateDto dto,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") int userSeq) {

        String requestId = cs.create(dto, userSeq);
        return ResponseEntity.ok(Map.of("requestId", requestId));
    }

    /** 프로젝트 정보 수정 */
    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> update(
            @RequestBody CreatorUpdateDto dto,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") int userSeq) {

        String requestId = cs.update(dto, userSeq);
        return ResponseEntity.ok(Map.of("requestId", requestId));
    }

    /** 프로젝트 중단 요청 */
    @PostMapping("/stop")
    public ResponseEntity<Map<String, String>> stop(
            @RequestBody CreatorStopDto dto,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") int userSeq) {

        String requestId = cs.stop(dto, userSeq);
        return ResponseEntity.ok(Map.of("requestId", requestId));
    }
}
