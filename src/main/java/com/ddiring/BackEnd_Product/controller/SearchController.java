package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.common.util.GatewayRequestHeaderUtils;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.dto.search.RequestSearch;
import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import com.ddiring.BackEnd_Product.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/product/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService ss;

    private void searchAdmin() {
        String role = GatewayRequestHeaderUtils.getRole();
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ForbiddenException("권한 없음 (required=ADMIN)");
        }
    }

    @GetMapping("/admin")
    public Page<RequestListDto> search(
            @RequestParam(value = "searchBy", required = false) RequestSearch.SearchBy searchBy,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) ProductRequestEntity.RequestType type,
            @RequestParam(value = "status", required = false) ProductRequestEntity.RequestStatus status,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            Pageable pageable) {

        searchAdmin();

        // ✅ page=1부터 시작하도록 보정
        int pageIndex = pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1;
        Pageable corrected = PageRequest.of(pageIndex, pageable.getPageSize(), pageable.getSort());

        RequestSearch sear = RequestSearch.builder()
                .searchBy(searchBy)
                .keyword(blankToNull(keyword))
                .type(type)
                .status(status)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        return ss.requestSearch(sear, corrected);
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
