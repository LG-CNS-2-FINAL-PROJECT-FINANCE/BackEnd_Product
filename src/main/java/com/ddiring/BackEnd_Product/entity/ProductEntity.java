package com.ddiring.BackEnd_Product.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product")
public class ProductEntity {

    @MongoId
    private String productId;

    @Version
    private Long version; // 자료형에 따라 값 달라짐

    @Indexed
    private int userSeq;

    private String title;
    private String summary;
    private String content;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private BigDecimal goalAmount;
    private BigDecimal minInvestment;

    private List<String> document;

    private ProductStatus status;

    //    private String account;

    private int viewCount = 0;

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
}
