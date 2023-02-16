package com.example.urlshortener.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.net.URL;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class ShortenedUrlDto {
    @Valid
    URL url;
    String shortUrl;
    @Email(message = "{errors.invalidEmail}")
    String email;
    LocalDateTime created;
    UrlStatus status = UrlStatus.ACTIVE;

    public ShortenedUrlDto(URL url) {
        this.url = url;
    }

    public ShortenedUrlDto(URL url, String email) {
        this(url);
        this.email = email;
    }
}
