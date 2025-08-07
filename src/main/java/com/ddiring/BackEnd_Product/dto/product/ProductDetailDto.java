package com.ddiring.BackEnd_Product.dto.product;

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
    private String projectId;
    private int userSeq;
    private String title;
    private String summary;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal goalAmount;
    private BigDecimal minInvestment;
    private List<String> document;
    private String account;
    private BigDecimal amount;
    private int viewCount;
    private ProductEntity.ProductStatus state;

    public static ProductDetailDto from(ProductEntity e) {
        return ProductDetailDto.builder()
                .projectId(e.getProjectId())
                .userSeq(e.getUserSeq())
                .title(e.getTitle())
                .summary(e.getSummary())
                .content(e.getContent())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .goalAmount(e.getGoalAmount())
                .minInvestment(e.getMinInvestment())
                .document(e.getDocument())
                .account(e.getAccount())
                .amount(e.getAmount())
                .viewCount(e.getViewCount())
                .state(e.getStatus())
                .build();
    }
}
