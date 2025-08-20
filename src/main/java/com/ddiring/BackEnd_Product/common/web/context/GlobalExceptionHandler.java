package com.ddiring.BackEnd_Product.common.web.context;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 인증 정보 누락/실패 -> 401
  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Map<String, String> handleIllegalState(IllegalStateException e) {
    return Map.of("error", e.getMessage());
  }

  // 권한 없음 -> 403
  @ExceptionHandler(ForbiddenException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN) // (클래스에 달려 있어도 중복 표기는 무방)
  public Map<String, String> handleForbidden(ForbiddenException e) {
    return Map.of("error", e.getMessage());
  }
}
