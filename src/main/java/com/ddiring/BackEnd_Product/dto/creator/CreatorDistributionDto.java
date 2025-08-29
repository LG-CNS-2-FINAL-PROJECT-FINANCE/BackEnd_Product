package com.ddiring.BackEnd_Product.dto.creator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorDistributionDto {

    @NotBlank(message="대상 프로젝트 ID 필요")
    private String projectId;

    @Positive
    @NotNull(message = "분배금을 설정하세요")
    private BigDecimal distributionAmount;

    @NotNull(message = "파일을 등록하세요")
    private List<String> document;
    private List<String> image;

    private String distributionSummary;
}
