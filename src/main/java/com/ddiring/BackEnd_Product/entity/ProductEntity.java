package com.ddiring.BackEnd_Product.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal percent;

    private List<String> document;

    private String smartContract;

    private int viewCount;

    @Builder.Default
    private Set<String> favorites = new HashSet<>();

    private ProductStatus status;

    private String reason;

//    @CreatedBy
//    private int createdId;
//    @CreatedDate
//    private LocalDateTime createdAt;
//    @LastModifiedBy
//    private int updatedId;
//    @LastModifiedDate
//    private LocalDateTime updatedAt;

    public enum ProductStatus {OPEN, HOLD, END}

    public int dDay() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    public int getFavoritesCount() {
        return favorites == null ? 0 : favorites.size();
    }

    public boolean isFavoritedBy(String userSeq) {
        return favorites != null && favorites.contains(userSeq);
    }
}
