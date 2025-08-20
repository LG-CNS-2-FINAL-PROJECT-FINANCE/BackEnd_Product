package com.ddiring.BackEnd_Product.common.web.context;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.common.exception.NotFound;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> notFound(NotFound e) {
        return Map.of("error", "not_found", "message", e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> forbidden(ForbiddenException e) {
        return Map.of("error", "forbidden", "message", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // @RequestBody DTO 검증 실패
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> invalid(MethodArgumentNotValidException e) {
        var fe = e.getBindingResult().getFieldErrors().stream().findFirst();
        return Map.of(
                "error", "validation_failed",
                "field", fe.map(err -> err.getField()).orElse(null),
                "message", fe.map(err -> err.getDefaultMessage()).orElse(e.getMessage())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class) // @RequestParam/@PathVariable 검증 실패
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> constraint(ConstraintViolationException e) {
        return Map.of("error", "constraint_violation", "message", e.getMessage());
    }

    @ExceptionHandler(Exception.class) // 나머지
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> etc(Exception e) {
        return Map.of("error", "internal_server_error");
    }
}
