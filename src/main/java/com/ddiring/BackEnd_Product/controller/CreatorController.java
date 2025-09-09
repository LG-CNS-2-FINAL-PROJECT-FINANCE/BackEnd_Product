package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.security.AuthUtils;
import com.ddiring.BackEnd_Product.dto.common.CreatorRequestResponse;
import com.ddiring.BackEnd_Product.dto.creator.*;
import com.ddiring.BackEnd_Product.service.CreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product/request")
@RequiredArgsConstructor
public class CreatorController {

    private final CreatorService cs;

    @PostMapping("/create")
    public ResponseEntity<CreatorRequestResponse> create(@RequestBody @Valid CreatorCreateDto dto) {
        String creatorSeq = AuthUtils.requireCreator();
        return ResponseEntity.ok(new CreatorRequestResponse(cs.create(dto, creatorSeq)));
    }

    @PostMapping("/update")
    public ResponseEntity<CreatorRequestResponse> update(@RequestBody @Valid CreatorUpdateDto dto) {
        String creatorSeq = AuthUtils.requireCreator();
        return ResponseEntity.ok(new CreatorRequestResponse(cs.update(dto, creatorSeq)));
    }

    @PostMapping("/stop")
    public ResponseEntity<CreatorRequestResponse> stop(@RequestBody @Valid CreatorStopDto dto) {
        String creatorSeq = AuthUtils.requireCreator();
        return ResponseEntity.ok(new CreatorRequestResponse(cs.stop(dto, creatorSeq)));
    }

    @PostMapping("/distribution")
    public ResponseEntity<CreatorRequestResponse> distribution (@RequestBody @Valid CreatorDistributionDto dto) {
        String creatorSeq = AuthUtils.requireCreator();
        return ResponseEntity.ok(new CreatorRequestResponse(cs.distribution(dto, creatorSeq)));
    }
}