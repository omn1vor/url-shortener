package com.example.urlshortener.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class ShortenedUrlTest {

    @Autowired
    Validator validator;
    private static final Locale defaultLocale = Locale.getDefault();
    @Autowired
    ShortenedUrl validShortenedUrl;

    @AfterEach
    void tearDown() {
        if (Locale.getDefault() != defaultLocale)
            Locale.setDefault(defaultLocale);
    }

    @Test
    public void testValidUrl() {
        Set<ConstraintViolation<ShortenedUrl>> violations = validator.validate(validShortenedUrl);
        assertTrue(violations.isEmpty());
    }
}