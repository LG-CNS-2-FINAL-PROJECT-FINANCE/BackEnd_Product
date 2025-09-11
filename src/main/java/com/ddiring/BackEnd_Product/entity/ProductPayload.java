package com.ddiring.BackEnd_Product.entity;

import com.ddiring.BackEnd_Product.dto.creator.CreatorDistributionDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorStopDto;
import com.ddiring.BackEnd_Product.dto.creator.CreatorUpdateDto;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPayload {
    private String projectId;  // UPDATE/STOP/DISTRIBUTION 대상
    private String nickname;
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

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal distributionAmount;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal distributionPercent;

    @Builder.Default
    private List<String> document = new ArrayList<>();
    @Builder.Default
    private List<String> image = new ArrayList<>();

    private String reason;     // UPDATE/STOP 대상
    private String distributionSummary;   // DISTRIBUTION 대상

    public static ProductPayload from(ProductEntity e) {
        return ProductPayload.builder()
                .projectId(e.getProjectId())
                .nickname(e.getNickname())
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
        if (dto.getTitle()        != null) this.title         = dto.getTitle();
        if (dto.getSummary()      != null) this.summary       = dto.getSummary();
        if (dto.getContent()      != null) this.content       = dto.getContent();
        if (dto.getStartDate()    != null) this.startDate     = dto.getStartDate();
        if (dto.getEndDate()      != null) this.endDate       = dto.getEndDate();
        if (dto.getGoalAmount()   != null) this.goalAmount    = dto.getGoalAmount();
        if (dto.getMinInvestment()!= null) this.minInvestment = dto.getMinInvestment();
        if (dto.getDocument() != null) {
            this.document.addAll(dto.getDocument()); // 새 문서 추가
            this.document = this.document.stream().distinct().toList();} // 중복 제거
        if (dto.getImage() != null) {
            this.image.addAll(dto.getImage()); // 새 이미지 추가
            this.image = this.image.stream().distinct().toList();} // 중복 제거
        if (dto.getReason()       != null) this.reason        = dto.getReason();
    }

    /* ---------- 정지 ---------- */
    public void stop(CreatorStopDto dto) {
        if (dto.getReason()       != null) this.reason   = dto.getReason();
        if (dto.getDocument() != null) {
            this.document.addAll(dto.getDocument()); // 새 문서 추가
            this.document = this.document.stream().distinct().toList();} // 중복 제거
        if (dto.getImage() != null) {
            this.image.addAll(dto.getImage()); // 새 이미지 추가
            this.image = this.image.stream().distinct().toList();} // 중복 제거
    }

    /* ---------- 분배요청 ---------- */
    public void distribution(CreatorDistributionDto dto) {
        if (dto.getDocument() != null) {
            this.document.addAll(dto.getDocument()); // 새 문서 추가
            this.document = this.document.stream().distinct().toList();} // 중복 제거
        if (dto.getImage() != null) {
            this.image.addAll(dto.getImage()); // 새 이미지 추가
            this.image = this.image.stream().distinct().toList();} // 중복 제거
        if (dto.getDistributionAmount()  != null) {this.distributionAmount  = dto.getDistributionAmount();}
        if (dto.getDistributionSummary() != null) {this.distributionSummary = dto.getDistributionSummary();}
    }
}
