package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
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

    public List<ProductListDto> getEndedProducts(Pageable pageable) {
        Criteria criteria = Criteria.where("status").is(ProductEntity.ProductStatus.END);
        Query query = new Query(criteria).with(pageable);

        List<ProductEntity> rows = mt.find(query, ProductEntity.class);
        return rows.stream()
                .map(ProductListDto::from) // DTO 매퍼 가정
                .toList();
    }
}