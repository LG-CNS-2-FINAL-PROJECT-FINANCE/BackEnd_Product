package com.ddiring.BackEnd_Product.dto.product;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListDto {
    private String projectId;
    private String userSeq;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private int deadline;
    private BigDecimal amount;
    private BigDecimal percent;
    private int viewCount;
    private ProductEntity.ProductStatus state;

    public static ProductListDto from(ProductEntity e) {
        return ProductListDto.builder()
                .projectId(e.getProjectId())
                .userSeq(e.getUserSeq())
                .title(e.getTitle())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .deadline(e.getDeadline())
                .amount(e.getAmount())
                .percent(e.getPercent())
                .viewCount(e.getViewCount())
                .state(e.getStatus())
                .build();
    }
}
