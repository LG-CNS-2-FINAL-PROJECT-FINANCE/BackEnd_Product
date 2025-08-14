package com.ddiring.BackEnd_Product.repository;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository
        extends MongoRepository<ProductEntity, String> {
    // 1. 전체 조회수 높은순
    List<ProductEntity> findTop10ByOrderByViewCountDesc();

    // 2. 마감기일이 남은것 중 조회수 높은순
    List<ProductEntity> findTop10ByEndDateAfterOrderByViewCountDesc(LocalDateTime now);

    // 3. 모금액 높은순
    List<ProductEntity> findTop10ByOrderByAmountDesc();
}
