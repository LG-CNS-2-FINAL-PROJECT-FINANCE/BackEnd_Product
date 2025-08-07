package com.ddiring.BackEnd_Product.dto.product;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListDto {
    private String projectId;
    private int userSeq;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal amount;
    private int viewCount;
    private ProductEntity.ProductStatus state;

    public static ProductListDto from(ProductEntity e) {
        return ProductListDto.builder()
                .projectId(e.getProjectId())
                .userSeq(e.getUserSeq())
                .title(e.getTitle())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .amount(e.getAmount())
                .viewCount(e.getViewCount())
                .state(e.getStatus())
                .build();
    }
}
