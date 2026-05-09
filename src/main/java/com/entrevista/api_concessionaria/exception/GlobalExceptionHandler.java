package com.entrevista.api_concessionaria.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceRunTimeException.class)
    public ResponseEntity<ErrorResponse> handleServiceRunTimeException(ServiceRunTimeException ex,
        HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request", 
            ex.getMessage(), 
            request.getRequestURI() 
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}