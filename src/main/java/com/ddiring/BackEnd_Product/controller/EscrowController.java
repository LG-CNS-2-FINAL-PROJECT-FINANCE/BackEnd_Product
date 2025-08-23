package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.dto.escrow.AmountDto;
import com.ddiring.BackEnd_Product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// dev부터는 필요 없을 수도
@RestController
@RequiredArgsConstructor
public class EscrowController {

    private final ProductService ps;

    @PostMapping("/api/balance")
    public ResponseEntity<Void> onAmountChanged(@RequestBody AmountDto dto) {
        ps.receiveAmount(dto); // 여기서 바로 호출
        return ResponseEntity.ok().build();
    }
}
