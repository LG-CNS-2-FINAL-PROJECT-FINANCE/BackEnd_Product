package com.ddiring.BackEnd_Product.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    private String id;

    private Integer productId;
    private String title;
    private String description;
    private String category;
    private Integer price;
    private Integer goalAmount;
    private Integer currentAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
