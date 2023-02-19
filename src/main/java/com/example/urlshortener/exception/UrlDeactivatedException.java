package com.example.urlshortener.exception;

public class UrlDeactivatedException extends ShortUrlException {
    public UrlDeactivatedException(String message) {
        super(message);
    }
}
