package com.ddiring.BackEnd_Product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRejectDto {

    @NotBlank
    private String requestId;

    private int adminSeq;

    @NotBlank
    private String reason;
}
