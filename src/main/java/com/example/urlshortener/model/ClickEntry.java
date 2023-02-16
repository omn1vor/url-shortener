package com.example.urlshortener.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CLICKS")
@Getter @Setter
public class ClickEntry {
    @Id @GeneratedValue
    private long id;
    @ManyToOne(optional = false)
    private ShortenedUrl url;
    private LocalDateTime date;
    private String ip;
}
