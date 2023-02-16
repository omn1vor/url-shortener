package com.example.urlshortener.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import java.net.URL;
import java.time.LocalDateTime;

@Entity
@Table(name = "URLS", indexes = @Index(columnList = "shortUrl"))
@Getter @Setter @NoArgsConstructor
public class ShortenedUrl {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Valid
    private URL url;
    private String shortUrl = "";
    @ManyToOne(optional = false)
    User author;
    LocalDateTime created;
    @Enumerated(EnumType.STRING)
    UrlStatus status = UrlStatus.ACTIVE;

    public String getEmail() {
        if (author == null)
            return "";
        return author.getEmail();
    }

}
