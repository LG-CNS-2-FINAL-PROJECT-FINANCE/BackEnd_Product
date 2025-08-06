package com.ddiring.BackEnd_Product.repository;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository
        extends MongoRepository<ProductEntity, String> {}
