package com.ddiring.BackEnd_Product.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminRejectDto {

    @NotBlank
    private String requestId;

    private String adminSeq;

    @NotBlank
    private String rejectReason;
}
