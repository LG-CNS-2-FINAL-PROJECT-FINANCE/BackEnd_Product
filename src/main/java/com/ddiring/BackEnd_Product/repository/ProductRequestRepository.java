package com.ddiring.BackEnd_Product.repository;

import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRequestRepository
        extends MongoRepository<ProductRequestEntity, String> {

    boolean existsByProjectIdAndRequestStatus(String projectId, ProductRequestEntity.RequestStatus requestStatus);

}