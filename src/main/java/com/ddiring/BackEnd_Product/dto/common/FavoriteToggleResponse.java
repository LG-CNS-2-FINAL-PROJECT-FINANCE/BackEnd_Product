package com.ddiring.BackEnd_Product.dto.common;

import lombok.*;

@Getter
@AllArgsConstructor
public class FavoriteToggleResponse {
    private String projectId;
    private boolean favorited;
}

