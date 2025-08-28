package com.ddiring.BackEnd_Product.dto.request;

import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestListDto {
    private String requestId;
    private String userSeq;

    private LocalDate startDate;
    private LocalDate endDate;

    private String projectId;  // UPDATE/STOP 대상
    private String title;

    private ProductRequestEntity.RequestType type;
    private ProductRequestEntity.RequestStatus status;
    private String adminSeq;

    public static RequestListDto from(ProductRequestEntity e) {
        return RequestListDto.builder()
                .requestId(e.getRequestId())
                .userSeq(e.getUserSeq())
                .startDate(e.getPayload().getStartDate())
                .endDate(e.getPayload().getEndDate())
                .projectId(e.getPayload().getProjectId())
                .title(e.getPayload().getTitle())
                .type(e.getType())
                .status(e.getStatus())
                .adminSeq(e.getAdminSeq())
                .build();
    }
}
