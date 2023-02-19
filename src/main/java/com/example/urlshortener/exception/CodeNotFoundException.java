package com.example.urlshortener.exception;

public class CodeNotFoundException extends ShortUrlException {
    public CodeNotFoundException(String message) {
        super(message);
    }
}
