package com.ddiring.BackEnd_Product.dto.admin;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminClosedDto {
    private String projectId;
    private String status;
    private String reason;
}

