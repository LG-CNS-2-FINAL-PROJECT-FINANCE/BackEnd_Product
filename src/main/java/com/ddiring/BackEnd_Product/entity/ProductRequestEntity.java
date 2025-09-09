package com.ddiring.BackEnd_Product.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "productRequest")
public class ProductRequestEntity {

    @MongoId
    private String requestId;

    private String projectId; //승인시 채워지도록 로직 구성
    private String userSeq;

    @Indexed
    private RequestType requestType;      // CREATE, UPDATE, STOP, DISTRIBUTION
    @Indexed
    private RequestStatus requestStatus;    // PENDING, APPROVED, REJECTED
    private ProductPayload payload;

    private String adminSeq;
    private String rejectReason;

    @CreatedDate
    private LocalDateTime createdAt; // 누가 했는지는 adminSeq

    public enum RequestType {CREATE, UPDATE, STOP, DISTRIBUTION}
    public enum RequestStatus {PENDING, APPROVED, REJECTED}
}
