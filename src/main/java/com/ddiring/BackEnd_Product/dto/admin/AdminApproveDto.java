package com.ddiring.BackEnd_Product.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminApproveDto {

    @NotBlank
    private String requestId;

//    private String adminId;
}
