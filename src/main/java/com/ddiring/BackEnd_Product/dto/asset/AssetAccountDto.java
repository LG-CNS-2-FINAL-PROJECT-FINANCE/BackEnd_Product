package com.ddiring.BackEnd_Product.dto.asset;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetAccountDto {
    private String projectId;
    private String title;
    private String account;
}