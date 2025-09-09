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
    public enum SearchBy { USER_SEQ, NICKNAME, TITLE }
    private String keyword;

    private ProductRequestEntity.RequestType requestType;
    private ProductRequestEntity.RequestStatus requestStatus;

    private LocalDate startDate;
    private LocalDate endDate;
}
