package com.example.urlshortener.service;

import com.example.urlshortener.UrlShortenerApplication;
import com.example.urlshortener.dto.CreateUrlRequest;
import com.example.urlshortener.dto.ShortenedUrlDto;
import com.example.urlshortener.exception.ShortUrlException;
import com.example.urlshortener.exception.UrlDeactivatedException;
import com.example.urlshortener.exception.UrlNotValidException;
import com.example.urlshortener.model.UrlStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UrlShortenerApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Profile("dev")
class ShortenedUrlServiceImplTest {

    @Autowired
    ShortenedUrlService shortenedUrlService;
    @Autowired
    UserService userService;
    @Autowired
    CreateUrlRequest validUrlRequest;

    final String existingCode = "severus";
    final String existingEmail = "hermione@thatschool.com";

    @Test
    void generateShortUrl() {
        assertEquals("test-generator", shortenedUrlService.generateShortUrl());
    }

    @Test
    void addUrl() {
        CreateUrlRequest request = new CreateUrlRequest(validUrlRequest);
        ShortenedUrlDto dto = shortenedUrlService.addUrl(request);

        assertNotEquals(0, dto.getId());
    }

    @Test
    void addBadUrl() {
        CreateUrlRequest request = new CreateUrlRequest(validUrlRequest);
        request.setUrl("hello");

        assertThrows(UrlNotValidException.class, () -> shortenedUrlService.addUrl(request));
    }

    @Test
    void addExistingUrl() {
        CreateUrlRequest request = new CreateUrlRequest(validUrlRequest);
        request.setCode(existingCode);
        assertThrows(ShortUrlException.class, () -> shortenedUrlService.addUrl(request));
    }

    @Test
    void addUrlNoShortUrl() {
        CreateUrlRequest request = new CreateUrlRequest(validUrlRequest);
        request.setCode("");
        ShortenedUrlDto dto = shortenedUrlService.addUrl(request);

        assertNotEquals(0, dto.getId());
        assertEquals("test-generator", dto.getCode());
    }

    @Test
    void activate() {
        ShortenedUrlDto dto = shortenedUrlService.activate(existingCode);
        assertEquals(UrlStatus.ACTIVE, dto.getStatus());
    }

    @Test
    void deactivate() {
        ShortenedUrlDto dto = shortenedUrlService.deactivate(existingCode);
        assertEquals(UrlStatus.INACTIVE, dto.getStatus());
    }

    @Test
    void validateActivated() {
        shortenedUrlService.activate(existingCode);
        assertDoesNotThrow(() -> shortenedUrlService.validateForwarding(existingCode));
    }

    @Test
    void validateDeactivated() {
        shortenedUrlService.deactivate(existingCode);
        assertThrows(UrlDeactivatedException.class, () -> shortenedUrlService.validateForwarding(existingCode));
    }

    @Test
    void findByUser() {
        List<ShortenedUrlDto> urls = shortenedUrlService.findByEmailNewFirst(existingEmail);
        assertEquals(2, urls.size());
    }

    @Test
    void findByPeriod() {
        LocalDateTime from = LocalDateTime.of(2022, 2, 1, 0, 0, 0);
        LocalDateTime to = LocalDateTime.of(2022, 2, 10, 0, 0, 0);
        List<ShortenedUrlDto> urls = shortenedUrlService.findByPeriod(from, to);
        assertEquals(1, urls.size());
    }


}