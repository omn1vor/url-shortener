package com.example.urlshortener.service.impl;

import com.example.urlshortener.model.ClickEntry;
import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.service.ClickTrackerService;
import com.example.urlshortener.service.storage.ClickEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClickTrackerServiceImpl implements ClickTrackerService {
    @Autowired
    private ClickEntryRepository clickEntryRepository;

    @Override
    public void linkClicked(ShortenedUrl url, String ip) {
        ClickEntry clickEntry = new ClickEntry();
        clickEntry.setUrl(url);
        clickEntry.setIp(ip);
        clickEntry.setDate(LocalDateTime.now());
        clickEntryRepository.save(clickEntry);
    }
}
