package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MongoTemplate mt;

    // 개인 요청 조회
    public Page<RequestListDto> getMyRequest(String userSeq, Pageable p) {
        // 조건: userSeq = 현재 로그인한 사용자
        Criteria criteria = Criteria.where("userSeq").is(userSeq)
                .and("requestStatus").is(ProductRequestEntity.RequestStatus.PENDING);

        // 페이징/정렬 포함하여 쿼리 생성
        Query query = new Query(criteria).with(p);

        // 데이터 조회 (실제 요청 목록)
        List<ProductRequestEntity> rows = mt.find(query, ProductRequestEntity.class);

        // 전체 개수 조회 (페이지네이션 totalElements 계산용)
        long total = mt.count(Query.of(query).limit(-1).skip(-1), ProductRequestEntity.class);

        // 엔티티 → DTO 변환
        List<RequestListDto> content = rows.stream()
                .map(RequestListDto::from) // DTO 매퍼 (static from 메서드) 가정
                .toList();

        // Page 객체로 리턴 (프론트에서 content/totalPages/... 사용 가능)
        return new PageImpl<>(content, p, total);
    }

    // 개인 상품 조회
    public Page<ProductListDto> getMyProduct(String userSeq, Pageable p) {
        Criteria criteria = Criteria.where("userSeq").is(userSeq);

        Query query = new Query(criteria).with(p);

        List<ProductEntity> rows = mt.find(query, ProductEntity.class);

        long total = mt.count(Query.of(query).limit(-1).skip(-1), ProductEntity.class);

        List<ProductListDto> content =rows.stream()
                .map(ProductListDto::from)
                .toList();

        return new PageImpl<>(content, p, total);
    }
}
