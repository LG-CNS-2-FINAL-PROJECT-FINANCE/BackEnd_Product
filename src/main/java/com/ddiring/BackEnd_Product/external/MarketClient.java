package com.ddiring.BackEnd_Product.external;

import com.ddiring.BackEnd_Product.dto.market.MarketDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "marketClient",
        url = "${market.base-url}"
)
public interface MarketClient {

    @GetMapping("/market/trade/{projectId}/history")
    List<MarketDto> getTradeHistory(@PathVariable("projectId") String projectId);
}