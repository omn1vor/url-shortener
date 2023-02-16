package com.example.urlshortener.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShortenedUrlTest {

    @Autowired
    Validator validator;
    private static final Locale defaultLocale = Locale.getDefault();
    ShortenedUrl url;

    @BeforeEach
    void setUp() throws MalformedURLException {
        url = new ShortenedUrl();
        url.setShortUrl("hello");
        url.setUrl(new URL("http:/google.com"));
    }

    @AfterEach
    void tearDown() {
        if (Locale.getDefault() != defaultLocale)
            Locale.setDefault(defaultLocale);
    }

    @Test
    public void testValidUrl() {
        Set<ConstraintViolation<ShortenedUrl>> violations = validator.validate(url);
        assertTrue(violations.isEmpty());
    }
}