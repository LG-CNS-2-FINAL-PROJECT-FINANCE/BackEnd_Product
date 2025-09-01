package com.ddiring.BackEnd_Product.dto.escrow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EscrowDistributionDto {
    private String account;
    private String userSeq;
    private BigDecimal distributionAmount;
}
