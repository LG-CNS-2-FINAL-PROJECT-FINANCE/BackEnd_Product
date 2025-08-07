package com.ddiring.BackEnd_Product.dto.product.escrow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequestDto {

    private String projectId;
}
