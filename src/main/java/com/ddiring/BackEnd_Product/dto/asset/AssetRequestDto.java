package com.ddiring.BackEnd_Product.dto.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRequestDto {
    private String projectId;
    private String title;
    private String account;
}
