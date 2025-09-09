package com.ddiring.BackEnd_Product.dto.market;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MarketDto {
    private Integer tradePrice;
    private Integer tokenQuantity;
    private LocalDateTime tradedAt;
}
