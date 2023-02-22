package com.example.urlshortener.controller;

import com.example.urlshortener.audit.LogRunTime;
import com.example.urlshortener.audit.UrlEventService;
import com.example.urlshortener.audit.event.UrlEventEntry;
import com.example.urlshortener.dto.ClickEntryDto;
import com.example.urlshortener.dto.CreateUrlRequest;
import com.example.urlshortener.dto.ShortenedUrlDto;
import com.example.urlshortener.dto.UserDto;
import com.example.urlshortener.service.ClickTrackerService;
import com.example.urlshortener.service.ShortenedUrlService;
import com.example.urlshortener.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
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
    @Autowired
    private UserService userService;

    @PostMapping()
    @Tag(name = "short-urls")
    public ShortenedUrlDto addUrl(@RequestBody @Valid CreateUrlRequest request) {
        return urlService.addUrl(request);
    }

    @PutMapping("{code}")
    @Tag(name = "short-urls")
    public ShortenedUrlDto addShortUrl(@RequestBody @Valid CreateUrlRequest request,
                                       @PathVariable String code) {
        return urlService.createOrReplace(request, code);
    }

    @PatchMapping(value = "/{code}", consumes = "application/json-patch+json")
    @Tag(name = "short-urls")
    public ShortenedUrlDto update(@RequestBody JsonNode patch,
                                  @PathVariable String code) {
        return urlService.update(code, patch);
    }

    @GetMapping("{code}")
    @LogRunTime
    @Tag(name = "short-urls")
    public ResponseEntity<?> goToUrl(@PathVariable String code, HttpServletRequest request) {
        ShortenedUrlDto dto = urlService.validateForwarding(code);
        clickTrackerService.linkClicked(code, request.getRemoteAddr());
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(dto.getUrl()))
                .build();
    }

    @GetMapping("{code}/events")
    @Tag(name = "audit")
    public List<UrlEventEntry> getAuditEvents(@PathVariable String code) {
        return urlEventService.findByCode(code);
    }

    @GetMapping("{code}/clicks")
    @Tag(name = "audit")
    public List<ClickEntryDto> getClicks(@PathVariable String code,
                                         @RequestParam(required = false) Integer limit) {
        return clickTrackerService.getClicks(code, limit);
    }

    @GetMapping("users")
    @Tag(name = "users")
    public List<UserDto> getUsers() {
        return userService.findAll();
    }

    @GetMapping("users/{email}")
    @Tag(name = "users")
    public long getCountOfUrlsByEmail(@PathVariable @Email String email) {
        return urlService.getCountOfUrls(email);
    }

    @GetMapping("users/{email}/links")
    @Tag(name = "users")
    public List<ShortenedUrlDto> getLinksByEmail(@PathVariable @Email String email,
                                @RequestParam(required = false)
                                @Pattern(
                                        regexp = "asc|desc",
                                        flags = Pattern.Flag.CASE_INSENSITIVE,
                                        message = "{sorting.not-match}") String sort) {
        if (sort == null || "desc".equalsIgnoreCase(sort)) {
            return urlService.findByEmailNewFirst(email);
        } else {
            return urlService.findByEmailOldFirst(email);
        }
    }
}
