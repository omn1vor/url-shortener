package com.example.urlshortener.controller;

import com.example.urlshortener.exception.CodeNotFoundException;
import com.example.urlshortener.exception.ShortUrlException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<?> handle(CodeNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<?> handle(ShortUrlException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<?> handle(ConstraintViolationException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }
}
