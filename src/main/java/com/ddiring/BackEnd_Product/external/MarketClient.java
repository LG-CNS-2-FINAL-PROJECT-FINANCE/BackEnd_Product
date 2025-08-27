package com.ddiring.BackEnd_Product.external;

import com.ddiring.BackEnd_Product.dto.market.TradeHistoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "market",
        url = "${market.base-url}"
)
public interface MarketClient {

    @GetMapping("/market/trade/{projectId}/history")
    List<TradeHistoryDto> getTradeHistory(@PathVariable("projectId") String projectId);
}