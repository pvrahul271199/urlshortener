package com.sayone.urlshortener.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlShortenerException.class)
    public ResponseEntity<String> handleException(RuntimeException e){
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
