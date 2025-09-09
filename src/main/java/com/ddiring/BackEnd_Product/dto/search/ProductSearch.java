package com.ddiring.BackEnd_Product.dto.search;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearch {

    private SearchBy searchBy;
    public enum SearchBy { PROJECT_ID, USER_SEQ, NICKNAME, TITLE }
    private String keyword;

    private ProductEntity.ProjectStatus projectStatus;
    private ProductEntity.ProjectVisibility projectVisibility;

    private LocalDate startDate;
    private LocalDate endDate;
}
