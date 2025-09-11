package com.ddiring.BackEnd_Product.dto.product;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import lombok.*;

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
    private String nickname;
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
    private BigDecimal distributionAmount;
    private BigDecimal distributionPercent;
    private String distributionSummary;
    private List<String> document;
    private List<String> image;
    private int viewCount;
    private boolean favorite;
    private ProductEntity.ProjectStatus projectStatus;
    private ProductEntity.ProjectVisibility projectVisibility;
    private String holdReason;

    private Integer tradePrice;

    public static ProductDetailDto from(ProductEntity e, String userSeq) {
        return ProductDetailDto.builder()
                .projectId(e.getProjectId())
                .userSeq(e.getUserSeq())
                .nickname(e.getNickname())
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
                .distributionAmount(e.getDistributionAmount())
                .distributionPercent(e.getDistributionPercent())
                .distributionSummary(e.getDistributionSummary())
                .document(e.getDocument())
                .image(e.getImage())
                .viewCount(e.getViewCount())
                .favorite(e.getFavorites().contains(userSeq))
                .projectStatus(e.getProjectStatus())
                .projectVisibility(e.getProjectVisibility())
                .holdReason(e.getHoldReason())
                .build();
    }
}
