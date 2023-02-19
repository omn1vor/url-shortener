package com.example.urlshortener.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ClickEntryDto {
    private LocalDateTime date;
    private String ip;
}
