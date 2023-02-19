package com.example.urlshortener.service.impl;

import com.example.urlshortener.audit.event.UrlEventEntry;
import com.example.urlshortener.dto.CreateUrlRequest;
import com.example.urlshortener.dto.ShortenedUrlDto;
import com.example.urlshortener.model.*;
import com.example.urlshortener.service.*;
import com.example.urlshortener.service.storage.ShortenedUrlRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ShortenedUrlServiceImpl implements ShortenedUrlService {

    @Autowired
    private UserService userService;
    @Autowired
    private ShortenedUrlRepository urlRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ShortUrlGenerator codeGenerator;
    @Autowired
    private ExceptionBuilder exceptionBuilder;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private UrlValidator urlValidator;

    @Override
    public String generateShortUrl() {
        return codeGenerator.generate();
    }

    @Override
    @Transactional
    public ShortenedUrlDto addUrl(CreateUrlRequest request) {
        urlValidator.validate(request.getUrl());
        String code = request.getCode() == null ? "" : request.getCode();

        if (code.isBlank()) {
            request.setCode(generateShortUrl());
        } else {
            if (urlRepository.existsByCode(code)) {
                throw exceptionBuilder.codeAlreadyExists(code);
            }
        }
        ShortenedUrl url = convertUrlToEntity(request);

        User user = userService.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            user = new User(request.getEmail());
        }
        url.setAuthor(user);

        url.setCreated(LocalDateTime.now());
        url = urlRepository.saveAndFlush(url);
        eventPublisher.publishEvent(UrlEventEntry.creation(url));
        return convertUrlToDto(url);
    }

    @Override
    @Transactional
    public ShortenedUrlDto createOrReplace(CreateUrlRequest request, String code) {
        urlValidator.validate(request.getUrl());
        Optional<ShortenedUrl> foundUrl = urlRepository.findByCode(code);
        if (foundUrl.isEmpty()) {
            CreateUrlRequest copy = new CreateUrlRequest(request);
            copy.setCode(code);
            return addUrl(copy);
        }

        ShortenedUrl url = foundUrl.get();
        url.setCode(code);
        url = urlRepository.saveAndFlush(url);
        eventPublisher.publishEvent(UrlEventEntry.modification(url, "Overwritten via PUT method"));
        return convertUrlToDto(url);
    }

    @Override
    public ShortenedUrlDto validateForwarding(String code) {
        ShortenedUrlDto dto = findByCode(code);
        if (dto.getStatus() != UrlStatus.ACTIVE) {
            throw exceptionBuilder.urlDeactivated(code);
        }
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortenedUrlDto> findByEmailOldFirst(String email) {
        return urlRepository.findAllByAuthor_Email(email, Sort.by(Sort.Order.asc("created")))
                .map(this::convertUrlToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortenedUrlDto> findByEmailNewFirst(String email) {
        return urlRepository.findAllByAuthor_Email(email, Sort.by(Sort.Order.desc("created")))
                .map(this::convertUrlToDto)
                .toList();
    }

    @Override
    public long getCountOfUrls(String email) {
        return urlRepository.countAllByAuthor_Email(email);
    }

    @Override
    @Transactional
    public ShortenedUrlDto update(String code, JsonNode patch) {
        ShortenedUrl url = urlRepository.findByCode(code)
                .orElseThrow(() -> exceptionBuilder.codeNotFound(code));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Set<String> allowedFields = Set.of("url", "author");
        Iterator<String> fieldNames = patch.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            if (!allowedFields.contains(fieldName)) {
                throw exceptionBuilder.illegalFieldModification(fieldName);
            }
        }

        try {
            url = patchUrl(url, patch);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw exceptionBuilder.wrongPatchFormat();
        }

        urlValidator.validate(url.getUrl());
        url = urlRepository.saveAndFlush(url);
        eventPublisher.publishEvent(UrlEventEntry.modification(url, "Updated via PATCH method"));
        return convertUrlToDto(url);
    }

    private ShortenedUrl patchUrl(ShortenedUrl url, JsonNode patch)
            throws JsonPatchException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        JsonMergePatch jsonPatch = JsonMergePatch.fromJson(patch);

        JsonNode originalUrl = objectMapper.valueToTree(url);
        JsonNode patched = jsonPatch.apply(originalUrl);
        return objectMapper.treeToValue(patched, ShortenedUrl.class);
    }

    private ShortenedUrl convertUrlToEntity(CreateUrlRequest dto) {
        return modelMapper.map(dto, ShortenedUrl.class);
    }

    private ShortenedUrlDto convertUrlToDto(ShortenedUrl url) {
        ShortenedUrlDto dto = modelMapper.map(url, ShortenedUrlDto.class);
        dto.setEmail(url.getEmail());
        return dto;
    }

    @Override
    public ShortenedUrlDto findByCode(String code) {
        ShortenedUrl url = urlRepository.findByCode(code)
                .orElseThrow(() -> exceptionBuilder.codeNotFound(code));

        return convertUrlToDto(url);
    }

    @Override
    @Transactional
    public ShortenedUrlDto activate(String code) {
        ShortenedUrl url = setStatus(code, UrlStatus.ACTIVE);
        eventPublisher.publishEvent(UrlEventEntry.modification(url, "set active"));
        return convertUrlToDto(url);
    }

    @Override
    @Transactional
    public ShortenedUrlDto deactivate(String code) {
        ShortenedUrl url = setStatus(code, UrlStatus.INACTIVE);
        eventPublisher.publishEvent(UrlEventEntry.deactivation(url));
        return convertUrlToDto(url);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortenedUrlDto> findByPeriod(LocalDateTime from, LocalDateTime to) {
        return urlRepository.findAllByCreatedBetweenOrderByCreated(from, to).stream()
                .map(this::convertUrlToDto)
                .toList();
    }

    private ShortenedUrl setStatus(String code, UrlStatus status) {
        ShortenedUrl url = urlRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("{errors.ShortUrlNotFound}: " + code));
        url.setStatus(status);
        return urlRepository.saveAndFlush(url);
    }
}
