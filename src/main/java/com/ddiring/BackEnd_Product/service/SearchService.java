package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.dto.search.ProductSearch;
import com.ddiring.BackEnd_Product.dto.search.RequestSearch;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MongoTemplate mt;

    /* ---------- 관리자 요청 검색 ---------- */
    public Page<RequestListDto> requestSearch(RequestSearch RS, Pageable P) {
        List<Criteria> conditions = new ArrayList<>();

        if(RS.getSearchBy() !=null && RS.getKeyword() != null) {
            switch (RS.getSearchBy()) {
                case USER_SEQ -> conditions.add(Criteria.where("userSeq").is(RS.getKeyword()));
                case NICKNAME -> {
                    String keyword = Pattern.quote(RS.getKeyword());
                    conditions.add(Criteria.where("nickname").regex(keyword, "i")); // 대소문자 무시, 부분 검색
                }
                case TITLE -> {
                    String keyword = Pattern.quote(RS.getKeyword());
                    conditions.add(Criteria.where("payload.title").regex(keyword, "i"));
                }
            }
        }

        if (RS.getRequestType() != null)   conditions.add(Criteria.where("requestType").is(RS.getRequestType()));
        if (RS.getRequestStatus() != null) conditions.add(Criteria.where("requestStatus").is(RS.getRequestStatus()));

        LocalDate fromD  = RS.getStartDate();  // null 허용
        LocalDate toDEx  = (RS.getEndDate() != null) ? RS.getEndDate().plusDays(1) : null;

        List<Criteria> period = new ArrayList<>();
        if (fromD != null) period.add(Criteria.where("payload.endDate").gte(fromD));
        if (toDEx != null) period.add(Criteria.where("payload.startDate").lt(toDEx));

        if (!period.isEmpty()) {
            conditions.add(new Criteria().andOperator(period.toArray(new Criteria[0])));
        }

        Criteria finalCriteria = conditions.isEmpty()
                ? new Criteria()
                : new Criteria().andOperator(conditions.toArray(new Criteria[0]));

        Query query = new Query(finalCriteria).with(P);
        List<ProductRequestEntity> rows = mt.find(query, ProductRequestEntity.class);

        long total = mt.count(Query.of(query).limit(-1).skip(-1), ProductRequestEntity.class);

        List<RequestListDto> content = rows.stream()
                .map(RequestListDto::from) // 이미 있는 정적 팩토리/매퍼로 가정
                .toList();

        return new PageImpl<>(content, P, total);
    }

    /* ---------- 관리자 상품 검색 ---------- */
    public PageImpl<ProductListDto> productSearch(ProductSearch PS, Pageable P) {
        List<Criteria> conditions = new ArrayList<>();

        if (PS.getSearchBy() != null && PS.getKeyword() != null) {
            switch (PS.getSearchBy()) {
                case PROJECT_ID -> conditions.add(Criteria.where("projectId").is(PS.getKeyword()));
                case USER_SEQ -> conditions.add(Criteria.where("userSeq").is(PS.getKeyword()));
                case NICKNAME -> {
                    String keyword = Pattern.quote(PS.getKeyword());
                    conditions.add(Criteria.where("nickname").regex(keyword, "i")); // 대소문자 무시, 부분 검색
                }
                case TITLE -> {
                    String keyword = Pattern.quote(PS.getKeyword());
                    conditions.add(Criteria.where("title").regex(keyword, "i")); // 대소문자 무시
                }
            }
        }

        if (PS.getProjectStatus() != null) {
            conditions.add(Criteria.where("projectStatus").is(PS.getProjectStatus()));
        }
        if (PS.getProjectVisibility() != null) {
            conditions.add(Criteria.where("projectVisibility").is(PS.getProjectVisibility()));
        }

        LocalDate fromD = PS.getStartDate();
        LocalDate toDEx = (PS.getEndDate() != null) ? PS.getEndDate().plusDays(1) : null;

        List<Criteria> period = new ArrayList<>();
        if (fromD != null) period.add(Criteria.where("endDate").gte(fromD));
        if (toDEx != null) period.add(Criteria.where("startDate").lt(toDEx));

        if (!period.isEmpty()) {
            conditions.add(new Criteria().andOperator(period.toArray(new Criteria[0])));
        }

        Criteria finalCriteria = conditions.isEmpty()
                ? new Criteria()
                : new Criteria().andOperator(conditions.toArray(new Criteria[0]));

        Query query = new Query(finalCriteria).with(P);
        List<ProductEntity> rows = mt.find(query, ProductEntity.class);
        long total = mt.count(Query.of(query).limit(-1).skip(-1), ProductEntity.class);

        List<ProductListDto> content = rows.stream()
                .map(ProductListDto::from)
                .toList();

        return new PageImpl<>(content, P, total);
    }

    // 미사용
    private boolean isNumeric(String s) {
        if (s == null) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return !s.isEmpty();
    }

    private LocalDateTime atStartOfDaySafe(LocalDate d) {
        return d == null ? null : d.atStartOfDay();
    }
    private LocalDateTime atEndExclusive(LocalDate d) {
        return d == null ? null : d.plusDays(1).atStartOfDay();
    }
}
