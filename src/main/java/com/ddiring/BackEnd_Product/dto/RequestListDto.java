package com.ddiring.BackEnd_Product.dto;

import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestListDto {
    private String requestId;
    private int userSeq;

    private String productId;  // UPDATE/STOP 대상
    private String title;

    private ProductRequestEntity.RequestType type;
    private ProductRequestEntity.RequestStatus status;
    private int admin;

    public static RequestListDto from(ProductRequestEntity e) {
        return RequestListDto.builder()
                .requestId(e.getRequestId())
                .userSeq(e.getUserSeq())
                .productId(e.getPayload().getProductId())
                .title(e.getPayload().getTitle())
                .type(e.getType())
                .status(e.getStatus())
                .admin(e.getAdminSeq())
                .build();
    }
}
