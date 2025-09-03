package com.ddiring.BackEnd_Product.common.exception;

import lombok.*;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
