package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.common.security.JwtAuthGuard;
import com.ddiring.BackEnd_Product.common.util.GatewayRequestHeaderUtils;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.service.FavoriteService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService fs;
    private final JwtAuthGuard guard; // JWT 인증/권한 가드

//    /** 즐겨찾기 토글 (USER 권한 필요) */
//    @PostMapping("/toggle/{id}")
//    public ResponseEntity<Map<String, Object>> toggle(@PathVariable("id") String projectId,
//                                                      @RequestHeader("Authorization") String auth) {
//
//        Claims c = guard.requireClaims(auth);
//        guard.requireAnyRole(c, "USER");
//        String userSeq = guard.requireUserSeq(c);
//
//        boolean nowFavorited = fs.toggle(projectId, userSeq);
//        return ResponseEntity.ok(Map.of(
//                "projectId", projectId,
//                "favorited", nowFavorited
//        ));
//    }
//
//    /** 내 즐겨찾기 목록 조회 (USER 권한 필요) */
//    @GetMapping("/favorite/me")
//    public ResponseEntity<List<ProductListDto>> myFavorites(
//            @RequestHeader("Authorization") String auth) {
//
//        Claims c = guard.requireClaims(auth);
//        guard.requireAnyRole(c, "USER");
//        String userSeq = guard.requireUserSeq(c);
//
//        List<ProductListDto> list = fs.listByUser(userSeq)
//                .stream()
//                .map(ProductListDto::from)
//                .toList();
//
//        return ResponseEntity.ok(list);
//    }

    /** 즐겨찾기 토글 (USER 권한 필요) */
    @PostMapping("/toggle/{id}")
    public ResponseEntity<Map<String, Object>> toggle(@PathVariable("id") String projectId) {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();
        String role = GatewayRequestHeaderUtils.getRole();

        if (!"USER".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=USER)");
        }

        boolean nowFavorited = fs.toggle(projectId, userSeq);
        return ResponseEntity.ok(Map.of(
                "projectId", projectId,
                "favorited", nowFavorited
        ));
    }

    /** 내 즐겨찾기 목록 조회 (USER 권한 필요) */
    @GetMapping("/favorite/me")
    public ResponseEntity<List<ProductListDto>> myFavorites() {
        String userSeq = GatewayRequestHeaderUtils.getUserSeq();
        String role = GatewayRequestHeaderUtils.getRole();

        if (!"USER".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=USER)");
        }

        List<ProductListDto> list = fs.listByUser(userSeq)
                .stream()
                .map(ProductListDto::from)
                .toList();

        return ResponseEntity.ok(list);
    }
}
