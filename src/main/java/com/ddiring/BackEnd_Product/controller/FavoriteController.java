package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.service.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService fs;

    private String currentUser(HttpServletRequest req) {
        String v = req.getHeader("userSeq");
        if (v == null || v.isBlank()) {
            throw new IllegalStateException("인증 정보가 없습니다"); // 추후 401 매핑 권장
        }
        return v.trim();
    }

    @PostMapping("/{id}/favorite/toggle")
    public Map<String, Object> toggle(@PathVariable("id") String productId, HttpServletRequest req) {
        String userId = currentUser(req);
        boolean nowFavorited = fs.toggle(productId, userId); // Service도 String 시그니처
        return Map.of("productId", productId, "favorited", nowFavorited);
    }

    @PostMapping("/{id}/favorite")
    public void add(@PathVariable("id") String productId, HttpServletRequest req) {
        fs.add(productId, currentUser(req));
    }

    @DeleteMapping("/{id}/favorite")
    public void remove(@PathVariable("id") String productId, HttpServletRequest req) {
        fs.remove(productId, currentUser(req));
    }

    @GetMapping("/favorite/me")
    public List<ProductListDto> myFavorites(HttpServletRequest req) {
        String userId = currentUser(req);
        return fs.listByUser(userId).stream()
                // 즐겨찾기 여부/개수까지 DTO에 넣고 싶으면 오버로드 사용 권장:
                // .map(e -> ProductListDto.from(e, userId))
                .map(ProductListDto::from)
                .toList();
    }
}

