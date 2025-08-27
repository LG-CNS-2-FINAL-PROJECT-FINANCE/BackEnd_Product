package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.market.TradeHistoryDto;
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
                    List<TradeHistoryDto> history = tc.getTradeHistory(e.getProjectId());

                    if (history != null && !history.isEmpty()) {
                        TradeHistoryDto latest = history.get(0); // 제일 최근 건만 사용
                        dto.setTradePrice(latest.getTradePrice());
                    } else {
                        dto.setTradePrice(null);
                    }

                    return dto;
                })
                .toList();
    }
}