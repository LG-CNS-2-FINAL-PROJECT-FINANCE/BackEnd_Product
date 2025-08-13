package com.ddiring.BackEnd_Product.dto.escrow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AmountDto {
    private String projectId;
    private BigDecimal balance; //모금액
    private BigDecimal percent; //달성률(소수점 첫째 자리)
}
