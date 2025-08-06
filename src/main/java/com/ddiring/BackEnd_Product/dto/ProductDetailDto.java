package com.ddiring.BackEnd_Product.dto;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailDto {
    private String productId;
    private int userSeq;
    private String title;
    private String summary;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal goalAmount;
    private BigDecimal minInvestment;
    private List<String> document;
    //    private String account;
    // 모금액 추가
    private int viewCount;
    private ProductEntity.ProductStatus state;


    public static ProductDetailDto from(ProductEntity e) {
        return ProductDetailDto.builder()
                .productId(e.getProductId())
                .userSeq(e.getUserSeq())
                .title(e.getTitle())
                .summary(e.getSummary())
                .content(e.getContent())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .goalAmount(e.getGoalAmount())
                .minInvestment(e.getMinInvestment())
                .document(e.getDocument())
                // 계좌 추가?
                // 모금액 추가
                .viewCount(e.getViewCount())
                .state(e.getStatus())
                .build();
    }
}
