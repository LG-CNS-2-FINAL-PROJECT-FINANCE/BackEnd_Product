package com.ddiring.BackEnd_Product.dto.search;

import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestSearch {
    public enum SearchBy { USER, TITLE}

    private SearchBy searchBy;
    private String keyword;

    private ProductRequestEntity.RequestType type;
    private ProductRequestEntity.RequestStatus status;

    private LocalDate startDate;
    private LocalDate endDate;
}
