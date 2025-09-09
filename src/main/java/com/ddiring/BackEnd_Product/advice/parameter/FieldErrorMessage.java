package com.ddiring.BackEnd_Product.advice.parameter;

import lombok.*;

@ToString
@Getter
public class FieldErrorMessage {
    private String field;
    private String message;

    @Builder
    public FieldErrorMessage(String field, String message) {
        this.field = field;
        this.message = message;
    }
}

