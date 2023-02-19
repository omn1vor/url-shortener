package com.example.urlshortener.exception;

public class UrlNotValidException extends ShortUrlException{
    public UrlNotValidException(String message) {
        super(message);
    }
}
