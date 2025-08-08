package com.ddiring.BackEnd_Product.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
    private int userSeq;

    private String title;
    private String summary;
    private String content;

    private LocalDate startDate;
    private LocalDate endDate;
    private int deadline;

    private BigDecimal goalAmount;
    private BigDecimal minInvestment;

    private List<String> document;

    private String account;
    private BigDecimal amount;

    private String smartContract;

    private int viewCount;

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
//        LocalDateTime now = LocalDateTime.now();
//        long daysBetween = ChronoUnit.DAYS.between(now, endDate);
//        return (int) daysBetween;
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }
}
