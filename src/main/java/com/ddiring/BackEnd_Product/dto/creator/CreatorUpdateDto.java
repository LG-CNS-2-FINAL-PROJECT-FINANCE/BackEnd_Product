package com.ddiring.BackEnd_Product.dto.creator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorUpdateDto {

    @NotBlank(message="대상 프로젝트 ID 필요")
    private String projectId;

    private String title;
    private String summary;
    private String content;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal goalAmount;
    private BigDecimal minInvestment;

    @NotNull(message = "파일을 등록하세요")
    private List<String> document;
    private List<String> image;

    @Size(max = 500)
    @NotBlank(message = "사유를 입력하세요")
    private String reason;
}
