package com.ddiring.BackEnd_Product.dto;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListDto {
    private String productId;
    private int userSeq;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    // 모금액 추가
    private int viewCount;
    private ProductEntity.ProductStatus state;

    public static ProductListDto from(ProductEntity e) {
        return ProductListDto.builder()
                .productId(e.getProductId())
                .userSeq(e.getUserSeq())
                .title(e.getTitle())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                // 모금액 추가
                .viewCount(e.getViewCount())
                .state(e.getStatus())
                .build();
    }
}
