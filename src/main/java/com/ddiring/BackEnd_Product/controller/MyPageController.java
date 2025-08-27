package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.util.GatewayRequestHeaderUtils;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.service.MyPageService;
import com.ddiring.BackEnd_Product.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/request")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService mps;
    private final RequestService rs;

    @GetMapping("/mypage")
    public Page<RequestListDto> getMyRequest(Pageable p) {
        // Gateway 또는 JWT에서 userSeq 추출
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();

        // 본인 요청 목록 조회
        return mps.getMyRequest(userSeq, p);
    }

    @PostMapping("/{requestId}/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable("requestId") String requestId) {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();

        rs.cancelRequest(requestId, userSeq);
        return ResponseEntity.noContent().build(); // 204 반환
    }
}
