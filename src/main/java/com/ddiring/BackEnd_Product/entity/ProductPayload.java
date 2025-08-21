package com.ddiring.BackEnd_Product.entity;

import com.ddiring.BackEnd_Product.dto.creator.CreatorStopDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPayload {
    private String projectId;  // UPDATE/STOP 대상
    private String title;
    private String summary;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private int deadline;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal goalAmount;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal minInvestment;

    @Builder.Default
    private List<String> document = new ArrayList<>();
    @Builder.Default
    private List<String> image = new ArrayList<>();

    private String reason;     // UPDATE/STOP 대상

    public static ProductPayload from(ProductEntity e) {
        return ProductPayload.builder()
                .projectId(e.getProjectId())
                .title(e.getTitle())
                .summary(e.getSummary())
                .content(e.getContent())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .deadline(e.getDeadline())
                .goalAmount(e.getGoalAmount())
                .minInvestment(e.getMinInvestment())
                .document(e.getDocument())
                .image(e.getImage())
                .reason(e.getReason())
                .build();
    }

    /* ---------- 부분수정 ---------- */
    public void update(CreatorUpdateDto dto) {
        if (dto.getTitle()        != null) this.title        = dto.getTitle();
        if (dto.getSummary()      != null) this.summary      = dto.getSummary();
        if (dto.getContent()      != null) this.content      = dto.getContent();
        if (dto.getStartDate()    != null) this.startDate    = dto.getStartDate();
        if (dto.getEndDate()      != null) this.endDate      = dto.getEndDate();
        if (dto.getGoalAmount()   != null) this.goalAmount   = dto.getGoalAmount();
        if (dto.getMinInvestment()!= null) this.minInvestment= dto.getMinInvestment();
        if (dto.getDocument()     != null) this.document     = dto.getDocument();
        if (dto.getImage()        != null) this.image        = dto.getImage();
        if (dto.getReason()       != null) this.reason       = dto.getReason();
    }

    /* ---------- 정지 ---------- */
    public void stop(CreatorStopDto dto) {
        if (dto.getReason()       != null) this.reason   = dto.getReason();
        if (dto.getDocument()     != null) this.document = dto.getDocument();
        if (dto.getImage()        != null) this.image    = dto.getImage();
    }
}
