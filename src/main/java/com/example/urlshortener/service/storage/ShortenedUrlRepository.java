package com.example.urlshortener.service.storage;

import com.example.urlshortener.model.ShortenedUrl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {
    Optional<ShortenedUrl> findByCode(String code);

    boolean existsByCode(String code);

    Stream<ShortenedUrl> findAllByAuthor_Email(String email, Sort sort);

    long countAllByAuthor_Email(String email);

    List<ShortenedUrl> findAllByCreatedBetweenOrderByCreated(LocalDateTime from, LocalDateTime to);

}

