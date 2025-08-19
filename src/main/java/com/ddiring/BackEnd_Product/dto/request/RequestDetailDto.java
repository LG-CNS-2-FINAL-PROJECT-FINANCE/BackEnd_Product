package com.ddiring.BackEnd_Product.dto.request;

import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDetailDto {
    private String requestId;
    private String userSeq;

    private String projectId;
    private String title;
    private String summary;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal goalAmount;
    private BigDecimal minInvestment;
    private List<String> document;
    private String reason;

    private ProductRequestEntity.RequestType type;
    private ProductRequestEntity.RequestStatus status;
    private String adminId;
    private String rejectReason;

    public static RequestDetailDto from(ProductRequestEntity e) {
        return RequestDetailDto.builder()
                .requestId(e.getRequestId())
                .userSeq(e.getUserSeq())
                .projectId(e.getPayload().getProjectId())
                .title(e.getPayload().getTitle())
                .summary(e.getPayload().getSummary())
                .content(e.getPayload().getContent())
                .startDate(e.getPayload().getStartDate())
                .endDate(e.getPayload().getEndDate())
                .goalAmount(e.getPayload().getGoalAmount())
                .minInvestment(e.getPayload().getMinInvestment())
                .document(e.getPayload().getDocument())
                .reason(e.getPayload().getReason())
                .type(e.getType())
                .status(e.getStatus())
                .adminId(e.getAdminId())
                .rejectReason(e.getRejectReason())
                .build();
    }
}
