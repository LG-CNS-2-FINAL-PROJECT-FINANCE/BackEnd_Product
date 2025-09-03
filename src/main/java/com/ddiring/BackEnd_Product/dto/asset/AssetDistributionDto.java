package com.ddiring.BackEnd_Product.dto.asset;

import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDistributionDto {
    private String projectId;
    private String userSeq;
    private BigDecimal distributionAmount;
}