package com.ddiring.BackEnd_Product.external;


import com.ddiring.BackEnd_Product.dto.asset.AssetRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "marketClient",
        url = "${asset.base-url}"
)
public interface AssetClient {
    @PostMapping("/asset")
    void asset(@RequestBody AssetRequestDto request);
}
