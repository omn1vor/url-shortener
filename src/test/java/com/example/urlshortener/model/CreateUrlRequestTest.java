package com.example.urlshortener.model;

import com.example.urlshortener.dto.CreateUrlRequest;
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
class CreateUrlRequestTest {

    @Autowired
    Validator validator;
    @Autowired
    CreateUrlRequest validUrlRequest;

    private static final Locale defaultLocale = Locale.getDefault();

    @AfterEach
    public void tearDown() {
        if (Locale.getDefault() != defaultLocale)
            Locale.setDefault(defaultLocale);
    }

    @Test
    public void testValidUrlDto() {
        Set<ConstraintViolation<CreateUrlRequest>> violations = validator.validate(validUrlRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidEmail() {
        CreateUrlRequest request = new CreateUrlRequest(validUrlRequest);
        request.setEmail("invalidEmail");
        Set<ConstraintViolation<CreateUrlRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        var violation = violations.stream().findAny().orElseThrow();
        assertEquals("Should be valid e-mail address", violation.getMessage());
    }

    @Test
    public void testInvalidEmailRu() {
        CreateUrlRequest request = new CreateUrlRequest(validUrlRequest);
        request.setEmail("invalidEmail");
        Locale.setDefault(Locale.forLanguageTag("ru"));
        Set<ConstraintViolation<CreateUrlRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        var violation = violations.stream().findAny().orElseThrow();
        assertEquals("Должен быть указан корректный e-mail", violation.getMessage());
    }

}