package com.ddiring.BackEnd_Product.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListInvestmentDto {

    private String title;
    private Integer goalAmount;
    private LocalDate endDate;
}
