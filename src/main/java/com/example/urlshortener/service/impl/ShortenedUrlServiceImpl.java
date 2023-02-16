package com.example.urlshortener.service.impl;

import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.model.ShortenedUrlDto;
import com.example.urlshortener.model.UrlStatus;
import com.example.urlshortener.model.User;
import com.example.urlshortener.service.ShortUrlGenerator;
import com.example.urlshortener.service.ShortenedUrlService;
import com.example.urlshortener.service.UserService;
import com.example.urlshortener.service.storage.ShortenedUrlRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShortenedUrlServiceImpl implements ShortenedUrlService {

    @Autowired
    private UserService userService;
    @Autowired
    private ShortenedUrlRepository urlRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ShortUrlGenerator shortUrlGenerator;

    @Override
    public String generateShortUrl() {
        return shortUrlGenerator.generate();
    }

    @Override
    @Transactional
    public ShortenedUrl addUrl(ShortenedUrlDto shortenedUrlDto) {
        ShortenedUrl url = convertUrlToEntity(shortenedUrlDto);
        url.setCreated(LocalDateTime.now());
        return urlRepository.save(url);
    }

    @Override
    @Transactional
    public ShortenedUrl addUrl(ShortenedUrl url) {
        url.setCreated(LocalDateTime.now());
        if (url.getShortUrl().isBlank())
            url.setShortUrl(generateShortUrl());
        return urlRepository.save(url);
    }

    private ShortenedUrl convertUrlToEntity(ShortenedUrlDto dto) {
        ShortenedUrl url = modelMapper.map(dto, ShortenedUrl.class);

        User user = userService.findByEmail(dto.getEmail())
                .orElse(new User(dto.getEmail()));
        url.setAuthor(user);

        if (url.getShortUrl().isBlank())
            url.setShortUrl(generateShortUrl());

        return url;
    }

    private ShortenedUrlDto convertUrlToDto(ShortenedUrl url) {
        ShortenedUrlDto dto = modelMapper.map(url, ShortenedUrlDto.class);
        dto.setEmail(url.getEmail());
        return dto;
    }

    @Override
    public ShortenedUrl activate(String shortUrl) {
        return setStatus(shortUrl, UrlStatus.ACTIVE);
    }

    @Override
    public ShortenedUrl deactivate(String shortUrl) {
        return setStatus(shortUrl, UrlStatus.INACTIVE);
    }

    @Override
    public Optional<ShortenedUrl> findById(long id) {
        return urlRepository.findById(id);
    }

    @Override
    public List<ShortenedUrl> findByUser(User user) {
        return urlRepository.findAllByAuthor(user);
    }

    @Override
    public List<ShortenedUrl> findByPeriod(LocalDateTime from, LocalDateTime to) {
        return urlRepository.findAllByCreatedBetweenOrderByCreated(from, to);
    }

    private ShortenedUrl setStatus(String shortUrl, UrlStatus status) {
        ShortenedUrl url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new IllegalArgumentException("{errors.ShortUrlNotFound}: " + shortUrl));
        url.setStatus(status);
        return urlRepository.save(url);
    }
}
