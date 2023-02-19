package com.example.urlshortener.service;

import com.example.urlshortener.dto.CreateUrlRequest;
import com.example.urlshortener.dto.ShortenedUrlDto;
import com.example.urlshortener.model.User;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.List;

public interface ShortenedUrlService {
    String generateShortUrl();

    ShortenedUrlDto addUrl(CreateUrlRequest request);

    ShortenedUrlDto createOrReplace(CreateUrlRequest request, String code);

    ShortenedUrlDto update(String code, JsonNode patch);

    ShortenedUrlDto activate(String code);

    ShortenedUrlDto deactivate(String code);

    ShortenedUrlDto findByCode(String code);

    List<ShortenedUrlDto> findByUser(User user);

    List<ShortenedUrlDto> findByPeriod(LocalDateTime from, LocalDateTime to);
}
