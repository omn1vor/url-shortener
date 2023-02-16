package com.example.urlshortener.service.storage;

import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {
    Optional<ShortenedUrl> findByShortUrl(String shortUrl);
    List<ShortenedUrl> findAllByAuthor(User user);
    List<ShortenedUrl> findAllByCreatedBetweenOrderByCreated(LocalDateTime from, LocalDateTime to);
}

