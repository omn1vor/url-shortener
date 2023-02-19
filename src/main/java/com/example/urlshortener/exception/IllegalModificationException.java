package com.example.urlshortener.exception;

public class IllegalModificationException extends ShortUrlException {
    public IllegalModificationException(String message) {
        super(message);
    }
}
