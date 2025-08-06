package com.ddiring.BackEnd_Product.entity;

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
    private String reason;     // STOP 대상

//    public Update toUpdate() {
//        Update u = new Update();
//        if (title        != null) u.set("title",         title);
//        if (summary      != null) u.set("summary",       summary);
//        if (content      != null) u.set("content",       content);
//        if (startDate    != null) u.set("startDate",     startDate);
//        if (endDate      != null) u.set("endDate",       endDate);
//        if (goalAmount   != null) u.set("goalAmount",    goalAmount);
//        if (minInvestment!= null) u.set("minInvestment", minInvestment);
//        if (document     != null) u.set("document",      document);
//        return u.inc("version", 1); // 버전 증가 기능 +1 비관적락
//    }
}
