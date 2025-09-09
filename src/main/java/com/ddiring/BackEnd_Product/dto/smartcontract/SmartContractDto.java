package com.ddiring.BackEnd_Product.dto.smartcontract;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmartContractDto {
    private String projectId;
    private String tokenName;
    private String tokenSymbol;
    private Long totalGoalAmount;
    private Long minAmount;
}
