package com.example.urlshortener.service.impl;

import com.example.urlshortener.model.ClickEntry;
import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.service.ClickTrackerService;
import com.example.urlshortener.service.storage.ShortenedUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Profile("prod")
public class ClickTrackerServiceImpl implements ClickTrackerService {
    @Autowired
    private ShortenedUrlRepository shortenedUrlRepository;
    @Autowired
    private KafkaTemplate<String, ClickEntry> kafkaTemplate;

    @Override
    public void linkClicked(String code, String ip) {
        Optional<ShortenedUrl> foundUrl = shortenedUrlRepository.findByCode(code);
        if (foundUrl.isEmpty()) {
            return;
        }

        ClickEntry clickEntry = new ClickEntry();
        clickEntry.setUrl(foundUrl.get());
        clickEntry.setIp(ip);
        clickEntry.setDate(LocalDateTime.now());

        kafkaTemplate.sendDefault(clickEntry);
    }
}
