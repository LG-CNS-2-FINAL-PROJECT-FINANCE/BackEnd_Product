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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

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
//
//    @GetMapping
//    public Page<RequestListDto> search(
//            @RequestParam(value = "searchBy", required = false) RequestSearch.SearchBy searchBy,
//            @RequestParam(value = "keyword", required = false) String keyword,
//            @RequestParam(value = "type", required = false) ProductRequestEntity.RequestType type,
//            @RequestParam(value = "status", required = false) ProductRequestEntity.RequestStatus status,
//            @RequestParam(value = "startDate", required = false) LocalDate startDate,
//            @RequestParam(value = "endDate", required = false) LocalDate endDate,
//            Pageable pageable) {
//
//        searchAdmin();
//
//        RequestSearch cond = RequestSearch.builder()
//                .searchBy(searchBy)
//                .keyword(blankToNull(keyword))
//                .type(type)
//                .status(status)
//                .startDate(startDate)
//                .endDate(endDate)
//                .build();
//
//        return ss.search(cond, pageable);
//    }
//
//    private String blankToNull(String s) {
//        return (s == null || s.isBlank()) ? null : s.trim();
//    }

    @GetMapping
    public Page<RequestListDto> search(
            @RequestParam(value = "searchBy", required = false) RequestSearch.SearchBy searchBy,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) ProductRequestEntity.RequestType type,
            @RequestParam(value = "status", required = false) ProductRequestEntity.RequestStatus status,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            Pageable pageable) {

        // 권한 검증 생략
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
