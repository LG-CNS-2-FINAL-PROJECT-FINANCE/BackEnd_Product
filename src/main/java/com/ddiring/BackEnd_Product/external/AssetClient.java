package com.ddiring.BackEnd_Product.external;

import com.ddiring.BackEnd_Product.common.response.dto.ApiResponseDto;
import com.ddiring.BackEnd_Product.dto.asset.AssetRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "assetClient",
        url = "${asset.base-url}"
)
public interface AssetClient {
    @PostMapping("/api/escrow/account")
    ApiResponseDto<String> asset(@RequestBody AssetRequestDto request);
}