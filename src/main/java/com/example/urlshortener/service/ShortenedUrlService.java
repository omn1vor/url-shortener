package com.example.urlshortener.service;

import com.example.urlshortener.dto.CreateUrlRequest;
import com.example.urlshortener.dto.ShortenedUrlDto;
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

    List<ShortenedUrlDto> findByPeriod(LocalDateTime from, LocalDateTime to);

    ShortenedUrlDto validateForwarding(String code);

    long getCountOfUrls(String email);

    List<ShortenedUrlDto> findByEmailOldFirst(String email);

    List<ShortenedUrlDto> findByEmailNewFirst(String email);
}
