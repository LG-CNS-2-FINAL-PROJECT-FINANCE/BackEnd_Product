package com.ddiring.BackEnd_Product.dto.creator;

import com.ddiring.BackEnd_Product.entity.ProductPayload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorCreateDto {

    @NotBlank(message = "제목을 입력하세요")
    private String title;
    @NotBlank(message = "내용을 입력하세요")
    private String summary;
    @NotBlank(message = "요약 정보를 입력하세요")
    private String content;

    @NotNull(message = "투자 시작 날짜를 입력하세요")
    private LocalDate startDate;
    @NotNull(message = "투자 종료 날짜를 입력하세요")
    private LocalDate endDate;

    @Positive
    @NotNull(message = "목표금액을 설정하세요")
    private BigDecimal goalAmount;
    @Positive
    @NotNull(message = "최소금액을 설정하세요")
    private BigDecimal minInvestment;

    @NotNull(message = "파일을 등록하세요")
    @Builder.Default
    private List<String> document = new ArrayList<>();
    @NotNull(message = "사진을 등록하세요")
    @Builder.Default
    private List<String> image = new ArrayList<>();

    public ProductPayload toPayload() {
        return ProductPayload.builder()
                .title(title)
                .summary(summary)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .goalAmount(goalAmount)
                .minInvestment(minInvestment)
                .document(document)
                .image(image)
                .build(); // 초기값 지정
    }
}
