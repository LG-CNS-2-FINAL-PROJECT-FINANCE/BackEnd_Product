package com.ddiring.BackEnd_Product.dto.market;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TradeHistoryDto {
    private Integer tradePrice;
    private Integer tokenQuantity;
    private LocalDateTime tradedAt;
}
