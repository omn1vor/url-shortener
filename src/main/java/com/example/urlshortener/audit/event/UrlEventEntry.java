package com.example.urlshortener.audit.event;

import com.example.urlshortener.model.ShortenedUrl;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor @Getter
@Table(name = "URL_EVENTS")
public class UrlEventEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private ShortenedUrl url;
    private LocalDateTime date;
    private UrlEventType type;
    private String description;

    public UrlEventEntry(ShortenedUrl url, UrlEventType type, String description) {
        this.url = url;
        this.type = type;
        this.description = description;
        this.date = LocalDateTime.now();
    }

    public static UrlEventEntry creation(ShortenedUrl url) {
        return new UrlEventEntry(url, UrlEventType.CREATED, "link created");
    }

    public static UrlEventEntry modification(ShortenedUrl url, String message) {
        return new UrlEventEntry(url, UrlEventType.MODIFIED, message);
    }

    public static UrlEventEntry deactivation(ShortenedUrl url) {
        return new UrlEventEntry(url, UrlEventType.DISABLED, "link disabled");
    }
}
