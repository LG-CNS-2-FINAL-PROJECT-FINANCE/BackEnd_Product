package com.ddiring.BackEnd_Product.api;

import com.ddiring.BackEnd_Product.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ApiProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByProductId(Integer productId);
}
