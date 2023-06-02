package com.ziqi.mlplatform.controller.advice;

import com.ziqi.mlplatform.exception.OperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OperationException.class)
    public ResponseEntity<Object> handleOperationException(OperationException ex, WebRequest request) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("status", HttpStatus.NOT_FOUND.value());
        errorMap.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
    }
}
