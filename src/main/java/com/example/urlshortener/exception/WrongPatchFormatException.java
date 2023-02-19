package com.example.urlshortener.exception;

public class WrongPatchFormatException extends ShortUrlException {
    public WrongPatchFormatException(String message) {
        super(message);
    }
}
