package com.ddiring.BackEnd_Product.dto;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewDto {

    private int viewCount;

    public static ViewDto from(ProductEntity e) {
        return ViewDto.builder()
                .viewCount(e.getViewCount())
                .build();
    }
}
