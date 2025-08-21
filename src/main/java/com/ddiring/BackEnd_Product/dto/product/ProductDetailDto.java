package com.ddiring.BackEnd_Product.dto.product;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailDto {
    private String projectId;
    private String userSeq;
    private String title;
    private String summary;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private int deadline;
    private String account;
    private BigDecimal goalAmount;
    private BigDecimal minInvestment;
    private BigDecimal amount;
    private BigDecimal percent;
    private List<String> document;
    private List<String> image;
    private int viewCount;
    private ProductEntity.ProductStatus state;
    private String holdReason;

    public static ProductDetailDto from(ProductEntity e) {
        return ProductDetailDto.builder()
                .projectId(e.getProjectId())
                .userSeq(e.getUserSeq())
                .title(e.getTitle())
                .summary(e.getSummary())
                .content(e.getContent())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .deadline(e.getDeadline())
                .account(e.getAccount())
                .goalAmount(e.getGoalAmount())
                .minInvestment(e.getMinInvestment())
                .amount(e.getAmount())
                .percent(e.getPercent())
                .document(e.getDocument())
                .image(e.getImage())
                .viewCount(e.getViewCount())
                .state(e.getStatus())
                .holdReason(e.getHoldReason())
                .build();
    }
}
