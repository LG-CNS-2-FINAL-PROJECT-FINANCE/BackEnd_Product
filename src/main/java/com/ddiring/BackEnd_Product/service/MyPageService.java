//package com.ddiring.BackEnd_Product.service;
//
//import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
//import com.ddiring.BackEnd_Product.entity.ProductEntity;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class MyPageService {
//
//    private final MongoTemplate mt;
//
//    public Page<RequestListDto> getMyRequest(String userSeq, Pageable p) {
//        Criteria criteria = Criteria.where("userSeq").is(userSeq);
//
//        Query query = new Query(criteria).with(p);
//
//        List<ProductEntity> rows = mt.find
//    }
//}
