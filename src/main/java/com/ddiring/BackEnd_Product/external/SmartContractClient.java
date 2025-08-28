//package com.ddiring.BackEnd_Product.external;
//
//import com.ddiring.BackEnd_Product.dto.smartcontract.SmartContractRequestDto;
//import com.ddiring.BackEnd_Product.dto.smartcontract.SmartContractResponseDto;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@FeignClient(
//        name = "smartContractClient",          // 빈 이름
//        url  = "${smartContract.base-url}"     // application-local.yml 값 그대로 사용
//)
//public interface SmartContractClient {
//    @PostMapping("/api/smartContract/create")
//    SmartContractResponseDto createSmartContract(@RequestBody SmartContractRequestDto request);
//}
