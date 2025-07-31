package com.ddiring.BackEnd_Product.api;

import com.ddiring.BackEnd_Product.dto.ListInvestmentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ApiProductController {

    private final ApiProductService apiProductService;

    @GetMapping("/{productId}/market")
    public ResponseEntity<ListInvestmentDto> getListInvestment(
            @PathVariable Integer productID) {
        ListInvestmentDto dto = apiProductService.getListInvestment(productID);
        return ResponseEntity.ok(dto);
    }
}
