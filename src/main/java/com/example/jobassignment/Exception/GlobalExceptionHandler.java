package com.example.jobassignment.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> responseStatusExceptionException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        Map<String, Object> body = Map.of(
                "error", Map.of(
                        "code", errorCode.name(),
                        "message", errorCode.getMessage()
                )
        );
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }
}
