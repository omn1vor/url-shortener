package com.example.urlshortener.service.impl;

import com.example.urlshortener.service.ExceptionBuilder;
import com.example.urlshortener.service.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class UrlValidatorImpl implements UrlValidator {
    @Autowired
    private ExceptionBuilder exceptionBuilder;

    @Override
    public void validate(String url) {
        try {
            URL realUrl = new URL(url);
            if (realUrl.getHost().isEmpty()) {
                throw exceptionBuilder.urlNotValid(url);
            }
        } catch (MalformedURLException e) {
            throw exceptionBuilder.urlNotValid(url);
        }
    }
}
