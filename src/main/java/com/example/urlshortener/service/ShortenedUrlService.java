package com.example.urlshortener.service;

import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.model.ShortenedUrlDto;
import com.example.urlshortener.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShortenedUrlService {
    String generateShortUrl();
    ShortenedUrl addUrl(ShortenedUrlDto shortenedUrlDto);
    ShortenedUrl addUrl(ShortenedUrl shortenedUrl);
    ShortenedUrl activate(String shortUrl);
    ShortenedUrl deactivate(String shortUrl);
    Optional<ShortenedUrl> findById(long id);
    List<ShortenedUrl> findByUser(User user);
    List<ShortenedUrl> findByPeriod(LocalDateTime from, LocalDateTime to);
}
