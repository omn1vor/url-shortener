package com.example.urlshortener.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Table(name = "URLS", indexes = @Index(columnList = "code", unique = true))
@Getter @Setter @NoArgsConstructor
public class ShortenedUrl {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotEmpty
    private String url;
    @Column(nullable = false, unique = true)
    private String code = "";
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private User author;
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private UrlStatus status = UrlStatus.ACTIVE;

    public String getEmail() {
        if (author == null)
            return "";
        return author.getEmail();
    }

}
