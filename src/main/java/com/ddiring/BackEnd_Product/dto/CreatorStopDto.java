package com.ddiring.BackEnd_Product.dto;

import com.ddiring.BackEnd_Product.entity.ProductPayload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorStopDto {

    @NotNull
    private String productId;

    @Size(max = 500)
    @NotBlank(message = "중단사유를 입력하세요")
    private String reason;

    public ProductPayload toPayload() {
        ProductPayload.ProductPayloadBuilder ppb = ProductPayload.builder()
                .productId(productId);
        if(reason !=null) ppb.reason(reason);
        return ppb.build();
    }
}
