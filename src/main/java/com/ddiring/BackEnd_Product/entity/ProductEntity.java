package com.ddiring.BackEnd_Product.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product")
public class ProductEntity {

    @MongoId
    private String projectId;

    private Long version;

    @Indexed
    private String userSeq;

    private String title;
    private String summary;
    private String content;

    private LocalDate startDate;
    private LocalDate endDate;
    private int deadline;

    private String account;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal goalAmount;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal minInvestment;
    @Builder.Default
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount = BigDecimal.ZERO;;
    @Builder.Default
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal percent = BigDecimal.ZERO;;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal distributionAmount;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal distributionPercent;

    @Builder.Default
    private List<String> document = new ArrayList<>();
    @Builder.Default
    private List<String> image = new ArrayList<>();

    private int viewCount;

    @Builder.Default
    private Set<String> favorites = new HashSet<>();

    private ProjectStatus projectStatus;
    private ProjectVisibility projectVisibility;

    private String reason;
    private String holdReason;
    private String distributionSummary;

//    @CreatedBy
//    private int createdId;
    @CreatedDate
    private LocalDateTime createdAt;
//    @LastModifiedBy
//    private int updatedId;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum ProjectStatus {
        OPEN,
        FUNDING_OPEN,  // 지금 사용 안함(안할 수도 있음)
        FUNDING_LOCKED,
        TRADING,
        DISTRIBUTION_READY,
        DISTRIBUTING,
        CLOSED,
        TEMPORARY_STOP
    }

    public enum ProjectVisibility {
        PUBLIC,
        HOLD
    }

    public int dDay() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }
}
