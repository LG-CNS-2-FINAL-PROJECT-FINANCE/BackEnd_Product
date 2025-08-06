package com.ddiring.BackEnd_Product.entity;

import com.ddiring.BackEnd_Product.dto.CreatorStopDto;
import com.ddiring.BackEnd_Product.dto.CreatorUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.query.Update;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPayload {
    private String productId;  // UPDATE/STOP 대상
    private String title;
    private String summary;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal goalAmount;
    private BigDecimal minInvestment;
    private List<String> document;
    private String reason;     // UPDATE/STOP 대상

    public static ProductPayload from(ProductEntity e) {
        return ProductPayload.builder()
                .productId(e.getProductId())
                .title(e.getTitle())
                .summary(e.getSummary())
                .content(e.getContent())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .goalAmount(e.getGoalAmount())
                .minInvestment(e.getMinInvestment())
                .document(e.getDocument())
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
        if (dto.getReason()       != null) this.reason       = dto.getReason();
    }

    /* ---------- 정지 ---------- */
    public void stop(CreatorStopDto dto) {
        if (dto.getReason()       != null) this.reason   = dto.getReason();
        if (dto.getDocument()     !=null) this.document = dto.getDocument();
    }
}
