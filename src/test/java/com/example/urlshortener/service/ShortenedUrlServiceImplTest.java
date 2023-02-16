package com.example.urlshortener.service;

import com.example.urlshortener.UrlShortenerApplication;
import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.model.UrlStatus;
import com.example.urlshortener.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UrlShortenerApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShortenedUrlServiceImplTest {

    @Autowired
    ShortenedUrlService shortenedUrlService;

    @Test
    void generateShortUrl() {
        ShortenedUrl url = shortenedUrlService.findById(1).orElseThrow();
        assertEquals("test-generator", shortenedUrlService.generateShortUrl());
    }

    @Test
    void addUrl() throws MalformedURLException {
        ShortenedUrl url = shortenedUrlService.findById(3).orElseThrow();
        User user = url.getAuthor();

        url = new ShortenedUrl();
        url.setUrl(new URL("https://www.google.com/"));
        url.setAuthor(user);
        url.setShortUrl("seve1");
        url = shortenedUrlService.addUrl(url);

        assertNotEquals(0, url.getId());
    }

    @Test
    void addUrlNoShortUrl() throws MalformedURLException {
        ShortenedUrl url = shortenedUrlService.findById(3).orElseThrow();
        User user = url.getAuthor();

        url = new ShortenedUrl();
        url.setUrl(new URL("https://www.google.com/"));
        url.setAuthor(user);
        url = shortenedUrlService.addUrl(url);

        assertNotEquals(0, url.getId());
        assertEquals("test-generator", url.getShortUrl());
    }

    @Test
    void activate() {
        ShortenedUrl url = shortenedUrlService.findById(1).orElseThrow();
        url = shortenedUrlService.activate(url.getShortUrl());
        assertEquals(UrlStatus.ACTIVE, url.getStatus());
    }

    @Test
    void deactivate() {
        ShortenedUrl url = shortenedUrlService.findById(2).orElseThrow();
        url = shortenedUrlService.deactivate(url.getShortUrl());
        assertEquals(UrlStatus.INACTIVE, url.getStatus());
    }

    @Test
    void findById() {
        Optional<ShortenedUrl> urlResult = shortenedUrlService.findById(1);
        assertTrue(urlResult.isPresent());
    }

    @Test
    void findByUser() {
        Optional<ShortenedUrl> urlResult = shortenedUrlService.findById(1);
        assertTrue(urlResult.isPresent());

        List<ShortenedUrl> urls = shortenedUrlService.findByUser(urlResult.get().getAuthor());
        assertEquals(2, urls.size());
    }

    @Test
    void findByPeriod() {
        LocalDateTime from = LocalDateTime.of(2023, 2, 1, 0, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 2, 10, 0, 0, 0);
        List<ShortenedUrl> urls = shortenedUrlService.findByPeriod(from, to);
        assertEquals(1, urls.size());
    }


}