package com.ddiring.BackEnd_Product.controller;

import com.ddiring.BackEnd_Product.common.security.AuthUtils;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.dto.search.ProductSearch;
import com.ddiring.BackEnd_Product.dto.search.RequestSearch;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
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
@RequestMapping("/api/product/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService ss;

    @GetMapping("/admin/request")
    public Page<RequestListDto> requestSearch(
            @RequestParam(value = "searchBy", required = false) RequestSearch.SearchBy searchBy,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "requestType", required = false) ProductRequestEntity.RequestType requestType,
            @RequestParam(value = "requestStatus", required = false) ProductRequestEntity.RequestStatus requestStatus,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            Pageable p) {

        AuthUtils.requireAdmin();

        RequestSearch request = RequestSearch.builder()
                .searchBy(searchBy)
                .keyword(blankToNull(keyword))
                .requestType(requestType)
                .requestStatus(requestStatus)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        return ss.requestSearch(request, p);
    }

    @GetMapping("/admin/product")
    public Page<ProductListDto> productSearch(
            @RequestParam(value = "searchBy", required = false) ProductSearch.SearchBy searchBy,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "projectStatus", required = false) ProductEntity.ProjectStatus projectStatus,
            @RequestParam(value = "projectVisibility", required = false) ProductEntity.ProjectVisibility projectVisibility,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            Pageable p) {

        AuthUtils.requireAdmin();

        ProductSearch product = ProductSearch.builder()
                .searchBy(searchBy)
                .keyword(blankToNull(keyword))
                .projectStatus(projectStatus)
                .projectVisibility(projectVisibility)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        return ss.productSearch(product, p);
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
