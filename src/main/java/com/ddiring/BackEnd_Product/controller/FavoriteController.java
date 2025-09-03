package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.security.AuthUtils;
import com.ddiring.BackEnd_Product.dto.common.FavoriteToggleResponse;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService fs;

    /** 즐겨찾기 토글 (USER 권한 필요) */
    @PostMapping("/toggle/{projectId}")
    public ResponseEntity<FavoriteToggleResponse> toggle(@PathVariable("projectId") String projectId) {
        String userSeq = AuthUtils.requireUser();
        boolean nowFavorited = fs.toggle(projectId, userSeq);
        FavoriteToggleResponse response = new FavoriteToggleResponse(projectId, nowFavorited);
        return ResponseEntity.ok(response);
    }

    /** 내 즐겨찾기 목록 조회 (USER 권한 필요) */
    @GetMapping("/me")
    public ResponseEntity<List<ProductListDto>> myFavorites() {
        String userSeq = AuthUtils.requireUser();
        List<ProductListDto> list = fs.listByUser(userSeq)
                .stream()
                .map(ProductListDto::from)
                .toList();

        return ResponseEntity.ok(list);
    }
}
