package com.ddiring.BackEnd_Product.common.exception;

import lombok.AccessLevel;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiError extends RuntimeException {
    protected String errorCode;
    protected String errorMessage;
}
