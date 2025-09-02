package com.ddiring.BackEnd_Product.dto.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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