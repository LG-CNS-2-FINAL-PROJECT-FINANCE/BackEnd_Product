package com.ddiring.BackEnd_Product.external;

import com.ddiring.BackEnd_Product.common.response.dto.ApiResponseDto;
import com.ddiring.BackEnd_Product.dto.smartcontract.SmartContractDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "smartContractClient",
        url = "${smartContract.base-url}"
)
public interface SmartContractClient {
    @PostMapping("/api/contract/deploy")
    ApiResponseDto<?> requestDeploy(@RequestBody SmartContractDto dto);
}
