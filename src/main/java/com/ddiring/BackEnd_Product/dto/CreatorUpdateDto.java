package com.ddiring.BackEnd_Product.dto;

import com.ddiring.BackEnd_Product.entity.ProductPayload;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorUpdateDto {

    @NotBlank(message="대상 프로젝트 ID 필요")
    private String productId;

    private String title;
    private String summary;
    private String content;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private BigDecimal goalAmount;
    private BigDecimal minInvestment;

    private List<String> document;

    public ProductPayload toPayload() {
            ProductPayload.ProductPayloadBuilder ppb = ProductPayload.builder()
                    .productId(productId);
            if(title          != null) ppb.title(title);
            if(summary        != null) ppb.summary(summary);
            if(content        != null) ppb.content(content);

            if (startDate     != null) ppb.startDate(startDate);
            if (endDate       != null) ppb.endDate(endDate);

            if (goalAmount    != null) ppb.goalAmount(goalAmount);
            if (minInvestment != null) ppb.minInvestment(minInvestment);

            if (document      != null) ppb.document(document);
            return ppb.build(); // 선택적 수정 가능
    }
}
