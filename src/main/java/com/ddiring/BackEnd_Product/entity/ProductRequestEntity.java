package com.ddiring.BackEnd_Product.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

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
    private RequestType type;      // CREATE, UPDATE, STOP
    @Indexed
    private RequestStatus status;    // PENDING, APPROVED, REJECTED
    private ProductPayload payload;

    private String adminId;
    private String rejectReason;

//    @CreatedBy
//    private int createdId;
//    @CreatedDate
//    private LocalDateTime createdAt;
//    @LastModifiedBy
//    private int updatedId;
//    @LastModifiedDate
//    private LocalDateTime updatedAt;

    public enum RequestType {CREATE, UPDATE, STOP}
    public enum RequestStatus {PENDING, APPROVED, REJECTED}
}
