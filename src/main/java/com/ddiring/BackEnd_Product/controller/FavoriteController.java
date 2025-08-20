package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.security.JwtAuthGuard;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.service.FavoriteService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService fs;
    private final JwtAuthGuard guard; // JWT 인증/권한 가드

    /** Authorization 헤더에서 USER 권한/유저 식별 */
    private String currentUser(String authHeader) {
        Claims c = guard.requireClaims(authHeader);      // 토큰 검증
        guard.requireAnyRole(c, "USER");                 // USER 권한만 허용
        return guard.requireUserSeq(c);                  // userSeq 추출
    }

    @PostMapping("/{id}/favorite/toggle")
    public ResponseEntity<Map<String, Object>> toggle(
            @PathVariable("id") String productId,
            @RequestHeader("Authorization") String auth) {

        String userSeq = currentUser(auth);
        boolean nowFavorited = fs.toggle(productId, userSeq);
        return ResponseEntity.ok(Map.of("productId", productId, "favorited", nowFavorited));
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<Void> add(
            @PathVariable("id") String productId,
            @RequestHeader("Authorization") String auth) {

        fs.add(productId, currentUser(auth));
        return ResponseEntity.noContent().build(); // 204
    }

    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<Void> remove(
            @PathVariable("id") String productId,
            @RequestHeader("Authorization") String auth) {

        fs.remove(productId, currentUser(auth));
        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("/favorite/me")
    public ResponseEntity<List<ProductListDto>> myFavorites(
            @RequestHeader("Authorization") String auth) {

        String userSeq = currentUser(auth);
        List<ProductListDto> list = fs.listByUser(userSeq).stream()
                .map(ProductListDto::from)
                .toList();
        return ResponseEntity.ok(list);
    }
}
