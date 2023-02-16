package com.example.urlshortener.audit.impl;

import com.example.urlshortener.audit.UrlEventRepository;
import com.example.urlshortener.audit.UrlEventService;
import com.example.urlshortener.audit.event.UrlEventEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlEventServiceImpl implements UrlEventService {
    @Autowired
    private UrlEventRepository urlEventRepository;

    @Override
    @EventListener()
    public void logEvent(UrlEventEntry eventEntry) {
        urlEventRepository.save(eventEntry);
    }

    public List<UrlEventEntry> findByPeriod(LocalDateTime from, LocalDateTime to) {
        return urlEventRepository.findAllByDateBetweenOrderByDate(from, to);
    }

    public List<UrlEventEntry> findLast100() {
        return urlEventRepository.findTop100ByOrderByDateDesc();
    }
}
