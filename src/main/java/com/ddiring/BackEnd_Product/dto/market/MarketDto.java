package com.ddiring.BackEnd_Product.dto.market;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MarketDto {
    private Integer tradePrice;
    private Integer tokenQuantity;
    private LocalDateTime tradedAt;
}
