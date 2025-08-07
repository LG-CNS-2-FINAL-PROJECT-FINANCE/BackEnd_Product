package com.ddiring.BackEnd_Product.dto.escrow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AmountDto {
    private String productId;
    private BigDecimal amount;
}
