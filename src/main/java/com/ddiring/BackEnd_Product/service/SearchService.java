package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.dto.search.RequestSearch;
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

    public Page<RequestListDto> requestSearch(RequestSearch RS, Pageable P) {
        List<Criteria> conditions = new ArrayList<>();

        if(RS.getSearchBy() !=null && RS.getKeyword() != null) {
            switch (RS.getSearchBy()) {
                case TITLE -> {
                    String keyword = Pattern.quote(RS.getKeyword());
                    conditions.add(Criteria.where("payload.title").regex(keyword, "i"));
                }
                case USER_SEQ -> conditions.add(Criteria.where("userSeq").is(RS.getKeyword()));
            }
        }
        // 타입/상태
        if (RS.getType() != null)   conditions.add(Criteria.where("type").is(RS.getType()));
        if (RS.getStatus() != null) conditions.add(Criteria.where("status").is(RS.getStatus()));

        // 기간 (start end 기준)
        LocalDate fromD  = RS.getStartDate();                         // null 허용
        LocalDate toDEx  = (RS.getEndDate() != null) ? RS.getEndDate().plusDays(1) : null;

        List<Criteria> period = new ArrayList<>();
        if (fromD != null) period.add(Criteria.where("payload.endDate").gte(fromD));
        if (toDEx != null) period.add(Criteria.where("payload.startDate").lt(toDEx));

        if (!period.isEmpty()) {
            conditions.add(new Criteria().andOperator(period.toArray(new Criteria[0])));
        }


        Criteria finalCriteria = conditions.isEmpty()
                ? new Criteria() // 전체
                : new Criteria().andOperator(conditions.toArray(new Criteria[0]));

        Query query = new Query(finalCriteria).with(P);
        List<ProductRequestEntity> rows = mt.find(query, ProductRequestEntity.class);

        long total = mt.count(Query.of(query).limit(-1).skip(-1), ProductRequestEntity.class);

        // 매핑 (기존 Mapper/생성자 사용)
        List<RequestListDto> content = rows.stream()
                .map(RequestListDto::from) // 이미 있는 정적 팩토리/매퍼로 가정
                .toList();

        return new PageImpl<>(content, P, total);
    }

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
