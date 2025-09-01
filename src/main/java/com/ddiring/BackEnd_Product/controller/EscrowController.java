package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.dto.escrow.AmountDto;
import com.ddiring.BackEnd_Product.dto.escrow.EscrowDistributionDto;
import com.ddiring.BackEnd_Product.service.DistributionService;
import com.ddiring.BackEnd_Product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product/escrow")
@RequiredArgsConstructor
public class EscrowController {

    private final ProductService ps;
    private final DistributionService ds;

    @PostMapping("/balance")
    public ResponseEntity<Void> onAmountChanged(@RequestBody AmountDto dto) {
        ps.receiveAmount(dto); // 여기서 바로 호출
        return ResponseEntity.ok().build();
    }

    @PostMapping("/distribution")
    public ResponseEntity<String> onDeposit(@RequestBody EscrowDistributionDto dto) {
        ds.handleDeposit(dto);
        return ResponseEntity.ok("distribution");
    }
}