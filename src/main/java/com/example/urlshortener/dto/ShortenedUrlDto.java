package com.example.urlshortener.dto;

import com.example.urlshortener.model.UrlStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ShortenedUrlDto {
    @JsonIgnore
    private long id;
    private String url;
    private String code;
    private String email;
    private UrlStatus status = UrlStatus.ACTIVE;
}
