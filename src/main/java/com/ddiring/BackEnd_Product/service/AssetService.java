package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.asset.AssetAccountDto;
import com.ddiring.BackEnd_Product.dto.asset.AssetDistributionDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.external.AssetClient;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final ProductRepository pr;
    private final AssetClient ac;

    /* ---------- Asset에 escrow 전송 ---------- */
    @Transactional
    public ProductEntity sendAssetAccount(AssetAccountDto dto) {
        if (dto == null || dto.getProjectId() == null) {
            throw new IllegalArgumentException("projectId가 필요합니다.");
        }

        ProductEntity pe = pr.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // DB 내용으로 최종 DTO를 확정하고 싶으면 여기서 dto 보정 가능
        // (예: dto.setTitle(pe.getTitle());)
        ac.assetAccount(dto); // 동기 호출
        return pe;
    }

    /* ---------- Asset 모금액 확인 ---------- */
    @Transactional
    public ProductEntity sendAssetDistribution(AssetDistributionDto dto) {
        ProductEntity pe = pr.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // Asset 서비스 호출 (동기)
        ac.assetDistribution(dto);
        return pe;
    }
}
