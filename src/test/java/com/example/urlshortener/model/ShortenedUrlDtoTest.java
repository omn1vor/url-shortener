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
class ShortenedUrlDtoTest {

    @Autowired Validator validator;
    private static final Locale defaultLocale = Locale.getDefault();
    ShortenedUrlDto dto;

    @BeforeEach
    public void SetUp() throws MalformedURLException {
        dto = new ShortenedUrlDto();
        dto.setShortUrl("hello");
        dto.setUrl(new URL("http:/google.com"));
        dto.setEmail("test@test.com");
    }

    @AfterEach
    public void tearDown() {
        if (Locale.getDefault() != defaultLocale)
            Locale.setDefault(defaultLocale);
    }

    @Test
    public void testValidUrlDto() {
        Set<ConstraintViolation<ShortenedUrlDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidEmail() {
        dto.setEmail("invalidEmail");
        Set<ConstraintViolation<ShortenedUrlDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        var violation = violations.stream().findAny().orElseThrow();
        assertEquals("Should be valid e-mail address", violation.getMessage());
    }

    @Test
    public void testInvalidEmailRu() {
        dto.setEmail("invalidEmail");
        Locale.setDefault(Locale.forLanguageTag("ru"));
        Set<ConstraintViolation<ShortenedUrlDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        var violation = violations.stream().findAny().orElseThrow();
        assertEquals("Должен быть указан корректный e-mail", violation.getMessage());
    }

}