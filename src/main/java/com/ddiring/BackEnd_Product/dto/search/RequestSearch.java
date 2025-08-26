package com.ddiring.BackEnd_Product.dto.search;

import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestSearch {

    private SearchBy searchBy;
    public enum SearchBy { userSeq, title }
    private String keyword;

    private ProductRequestEntity.RequestType type;
    private ProductRequestEntity.RequestStatus status;

    private LocalDate startDate;
    private LocalDate endDate;
}
