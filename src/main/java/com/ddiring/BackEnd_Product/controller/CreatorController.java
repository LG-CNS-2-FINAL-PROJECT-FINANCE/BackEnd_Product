package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.security.JwtAuthGuard;
import com.ddiring.BackEnd_Product.dto.creator.CreatorCreateDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorStopDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorUpdateDto;
import com.ddiring.BackEnd_Product.service.CreatorService;
import io.jsonwebtoken.Claims;
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
    private final JwtAuthGuard guard;

    @PostMapping("/create")
    public ResponseEntity<Map<String,String>> create(@RequestHeader("Authorization") String auth,
                                                     @RequestBody @Valid CreatorCreateDto dto) {
        Claims c = guard.requireClaims(auth);
        guard.requireAnyRole(c, "CREATOR");
        String userSeq = guard.requireUserSeq(c);
        return ResponseEntity.ok(Map.of("requestId", cs.create(dto, userSeq)));
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String,String>> update(@RequestHeader("Authorization") String auth,
                                                     @RequestBody @Valid CreatorUpdateDto dto) {
        Claims c = guard.requireClaims(auth);
        guard.requireAnyRole(c, "CREATOR");
        String userSeq = guard.requireUserSeq(c);
        return ResponseEntity.ok(Map.of("requestId", cs.update(dto, userSeq)));
    }

    @PostMapping("/stop")
    public ResponseEntity<Map<String,String>> stop(@RequestHeader("Authorization") String auth,
                                                   @RequestBody @Valid CreatorStopDto dto) {
        Claims c = guard.requireClaims(auth);
        guard.requireAnyRole(c, "CREATOR");
        String userSeq = guard.requireUserSeq(c);
        return ResponseEntity.ok(Map.of("requestId", cs.stop(dto, userSeq)));
    }
}