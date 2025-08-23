package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.common.util.GatewayRequestHeaderUtils;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.dto.search.RequestSearch;
import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import com.ddiring.BackEnd_Product.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService ss;

    private void  searchAdmin() {
        String role = GatewayRequestHeaderUtils.getRole();
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=ADMIN)");
        }
    }


    public Page<RequestListDto> search(
            @RequestParam(required = false) RequestSearch.SearchBy searchBy,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ProductRequestEntity.RequestType type,
            @RequestParam(required = false) ProductRequestEntity.RequestStatus status,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endDate,
            Pageable pageable) {

        searchAdmin();

        RequestSearch cond = RequestSearch.builder()
                .searchBy(searchBy)
                .keyword(blankToNull(keyword))
                .type(type)
                .status(status)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        return ss.search(cond, pageable);
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
