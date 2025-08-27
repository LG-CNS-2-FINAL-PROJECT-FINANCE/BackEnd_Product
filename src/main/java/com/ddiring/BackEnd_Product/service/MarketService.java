package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.market.MarketDto;
import com.ddiring.BackEnd_Product.dto.product.ProductDetailDto;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.external.MarketClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final MongoTemplate mt;
    private final MarketClient tc;

    public List<ProductListDto> getEndedProducts(Pageable pageable) {
        Criteria criteria = Criteria.where("status").is(ProductEntity.ProductStatus.END);
        Query query = new Query(criteria).with(pageable);

        List<ProductEntity> rows = mt.find(query, ProductEntity.class);
        return rows.stream()
                .map(e -> {
                    ProductListDto dto = ProductListDto.from(e);

                    // trade-service 호출해서 최근 거래 가져오기
                    List<MarketDto> history = tc.getTradeHistory(e.getProjectId());

                    if (history != null && !history.isEmpty()) {
                        MarketDto latest = history.get(history.size()-1); // 제일 최근 건만 사용
                        dto.setTradePrice(latest.getTradePrice());
                    } else {
                        dto.setTradePrice(null);
                    }

                    return dto;
                })
                .toList();
    }

    public ProductDetailDto getEndedProductDetail(String projectId, String userSeq) {
        // 1) DB에서 상품 조회
        ProductEntity e = mt.findById(projectId, ProductEntity.class);
        if (e == null || e.getStatus() != ProductEntity.ProductStatus.END) {
            throw new IllegalArgumentException("존재하지 않거나 마감되지 않은 상품입니다: " + projectId);
        }

        // 2) 기본 변환 (favorite 포함)
        ProductDetailDto dto = ProductDetailDto.from(e, userSeq);

        // 3) trade-service 호출해서 최근 거래 붙이기
        List<MarketDto> history = tc.getTradeHistory(projectId);
        if (history != null && !history.isEmpty()) {
            MarketDto latest = history.get(history.size() - 1); // 최근 거래
            dto.setTradePrice(latest.getTradePrice());
        }

        return dto;
    }
}