package com.example.urlshortener.controller;

import com.example.urlshortener.audit.UrlEventService;
import com.example.urlshortener.audit.event.UrlEventEntry;
import com.example.urlshortener.dto.ClickEntryDto;
import com.example.urlshortener.dto.CreateUrlRequest;
import com.example.urlshortener.dto.ShortenedUrlDto;
import com.example.urlshortener.service.ClickTrackerService;
import com.example.urlshortener.service.ShortenedUrlService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/")
@Validated
public class ShortenedUrlController {

    @Autowired
    private ShortenedUrlService urlService;
    @Autowired
    private UrlEventService urlEventService;
    @Autowired
    private ClickTrackerService clickTrackerService;

    @PostMapping()
    public ShortenedUrlDto addUrl(@RequestBody @Valid CreateUrlRequest request) {
        return urlService.addUrl(request);
    }

    @PutMapping("{code}")
    public ShortenedUrlDto addShortUrl(@RequestBody @Valid CreateUrlRequest request,
                                                   @PathVariable String code) {
        return urlService.createOrReplace(request, code);
    }

    @PatchMapping(value = "/{code}", consumes = "application/json-patch+json")
    public ShortenedUrlDto update(@RequestBody JsonNode patch,
                                              @PathVariable String code) {
        return urlService.update(code, patch);
    }

    @GetMapping("{code}")
    public ResponseEntity<?> goToUrl(@PathVariable String code, HttpServletRequest request) {
        ShortenedUrlDto dto = urlService.findByCode(code);
        clickTrackerService.linkClicked(code, request.getRemoteAddr());
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(dto.getUrl()))
                .build();
    }

    @GetMapping("{code}/events")
    public List<UrlEventEntry> getAuditEvents(@PathVariable String code) {
        return urlEventService.findByCode(code);
    }

    @GetMapping("{code}/clicks")
    public List<ClickEntryDto> getClicks(@PathVariable String code,
                                         @RequestParam(required = false) @Max(100) Integer limit) {
        return clickTrackerService.getClicks(code, limit);
    }

}
