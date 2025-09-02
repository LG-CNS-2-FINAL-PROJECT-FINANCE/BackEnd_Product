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
public class ProductListDto {
    private String projectId;
    private String userSeq;
    private String nickname;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private int deadline;
    private BigDecimal amount;
    private BigDecimal percent;
    private List<String> image;
    private int viewCount;
    private ProductEntity.ProjectStatus projectStatus;
    private ProductEntity.ProjectVisibility projectVisibility;

    private Integer tradePrice;

    public static ProductListDto from(ProductEntity e) {
        return ProductListDto.builder()
                .projectId(e.getProjectId())
                .userSeq(e.getUserSeq())
                .nickname(e.getNickname())
                .title(e.getTitle())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .deadline(e.getDeadline())
                .amount(e.getAmount())
                .percent(e.getPercent())
                .image(e.getImage())
                .viewCount(e.getViewCount())
                .projectStatus(e.getProjectStatus())
                .projectVisibility(e.getProjectVisibility())
                .build();
    }
}
