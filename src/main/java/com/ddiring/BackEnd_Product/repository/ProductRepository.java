package com.ddiring.BackEnd_Product.repository;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository
        extends MongoRepository<ProductEntity, String> {
    // ‣ 열려 있는 상품만 정렬 조회
    List<ProductEntity> findByStatus(ProductEntity.ProductStatus status, Sort sort);

    // (Pageable 기반이 필요하면)
    Page<ProductEntity> findByStatus(ProductEntity.ProductStatus status, Pageable pageable);

}
