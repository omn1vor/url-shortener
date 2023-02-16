package com.example.urlshortener.audit;

import com.example.urlshortener.audit.event.UrlEventEntry;

import java.time.LocalDateTime;
import java.util.List;

public interface UrlEventService {
    void logEvent(UrlEventEntry eventEntry);
    List<UrlEventEntry> findByPeriod(LocalDateTime from, LocalDateTime to);
    List<UrlEventEntry> findLast100();
}
