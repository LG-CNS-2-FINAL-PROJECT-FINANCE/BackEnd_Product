package com.ddiring.BackEnd_Product.dto.escrow;

import lombok.*;

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
