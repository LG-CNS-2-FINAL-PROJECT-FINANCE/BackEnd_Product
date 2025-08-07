package com.ddiring.BackEnd_Product.dto.product.escrow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AmountDto {
    private String projectId;
    private BigDecimal balance;
}
