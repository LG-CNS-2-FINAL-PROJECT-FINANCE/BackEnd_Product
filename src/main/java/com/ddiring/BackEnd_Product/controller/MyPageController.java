package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.util.GatewayRequestHeaderUtils;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.service.MyPageService;
import com.ddiring.BackEnd_Product.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService mps;
    private final RequestService rs;

    @GetMapping("/request/myPage")
    public Page<RequestListDto> getMyRequest(Pageable p) {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();
        return mps.getMyRequest(userSeq, p);
    }

    @PostMapping("/request/cancel/{requestId}")
    public ResponseEntity<Void> cancelRequest(@PathVariable("requestId") String requestId) {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();
        rs.cancelRequest(requestId, userSeq);
        return ResponseEntity.noContent().build(); // 204 반환
    }

    @GetMapping("/product/myPage")
    public Page<ProductListDto> getMyProduct(Pageable p) {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();
        return  mps.getMyProduct(userSeq, p);
    }
}
