package com.ddiring.BackEnd_Product.service;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewService {

    private final MongoTemplate mt;

    public void viewCount(String productId) {
//        Query q = new Query(Criteria.where("_id").is(productId));
//        Update u = new Update().inc("viewCount", 1);
//        mt.updateFirst(q, u, ProductEntity.class);
        mt.getCollection("product") // 실제 컬렉션 이름
                .updateOne(
                        new Document("_id", productId),
                        new Document("$inc", new Document("viewCount", 1))
                );
    }
}
