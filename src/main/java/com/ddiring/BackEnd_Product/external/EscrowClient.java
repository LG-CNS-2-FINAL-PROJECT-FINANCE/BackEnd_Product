package com.ddiring.BackEnd_Product.external;

import com.ddiring.BackEnd_Product.dto.escrow.AccountRequestDto;
import com.ddiring.BackEnd_Product.dto.escrow.AccountResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "escrowClient",          // 빈 이름
        url  = "${escrow.base-url}"     // application-local.yml 값 그대로 사용
)
public interface EscrowClient {

    @PostMapping("/api/escrow/create")
    AccountResponseDto createAccount(@RequestBody AccountRequestDto request);
}
