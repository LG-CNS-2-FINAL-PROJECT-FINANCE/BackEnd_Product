package com.ddiring.BackEnd_Product.common.exception;

public class BadParameter extends ClientError {
    public BadParameter(String message) {
        this.errorCode = "BadParameter";
        this.errorMessage = message;
    }
}
