package com.ddiring.BackEnd_Product.dto.request;

import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestListDto {
    private String requestId;
    private String userSeq;
    private String nickname;

    private String projectId;  // UPDATE/STOP 대상
    private String title;

    private LocalDate startDate;
    private LocalDate endDate;

    private List<String> image = new ArrayList<>();

    private ProductRequestEntity.RequestType requestType;
    private ProductRequestEntity.RequestStatus requestStatus;
    private String adminSeq;

    public static RequestListDto from(ProductRequestEntity e) {
        return RequestListDto.builder()
                .requestId(e.getRequestId())
                .userSeq(e.getUserSeq())
                .nickname(e.getPayload().getNickname())
                .projectId(e.getPayload().getProjectId())
                .title(e.getPayload().getTitle())
                .startDate(e.getPayload().getStartDate())
                .endDate(e.getPayload().getEndDate())
                .image(e.getPayload().getImage())
                .requestType(e.getRequestType())
                .requestStatus(e.getRequestStatus())
                .adminSeq(e.getAdminSeq())
                .build();
    }
}
