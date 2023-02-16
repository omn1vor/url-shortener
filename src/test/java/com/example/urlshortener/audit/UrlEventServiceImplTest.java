package com.example.urlshortener.audit;

import com.example.urlshortener.UrlShortenerApplication;
import com.example.urlshortener.audit.event.UrlEventEntry;
import com.example.urlshortener.audit.event.UrlEventType;
import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.model.User;
import com.example.urlshortener.service.ShortenedUrlService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UrlShortenerApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UrlEventServiceImplTest {

    @Autowired
    UrlEventService urlEventService;

    @Autowired
    ShortenedUrlService shortenedUrlService;

    @Test
    public void testLogging() throws MalformedURLException {
        long numberOfEvents = urlEventService.findLast100().size();

        User existingUser = shortenedUrlService.findById(1).get().getAuthor();

        // Adding link
        ShortenedUrl url = new ShortenedUrl();
        url.setShortUrl("gagaga");
        url.setUrl(new URL("https://www.google.com/"));
        url.setAuthor(existingUser);
        shortenedUrlService.addUrl(url);

        List<UrlEventEntry> events = urlEventService.findLast100();
        assertEquals(events.get(0).getType(), UrlEventType.CREATED);

        long newNumberOfEvents = events.size();
        assertEquals(numberOfEvents + 1, newNumberOfEvents);

        // Deactivating link
        url = shortenedUrlService.deactivate(url.getShortUrl());
        numberOfEvents = newNumberOfEvents;

        events = urlEventService.findLast100();
        assertEquals(events.get(0).getType(), UrlEventType.DISABLED);

        newNumberOfEvents = events.size();
        assertEquals(numberOfEvents + 1, newNumberOfEvents);

        // Modifying link
        shortenedUrlService.activate(url.getShortUrl());
        numberOfEvents = newNumberOfEvents;

        events = urlEventService.findLast100();
        assertEquals(events.get(0).getType(), UrlEventType.MODIFIED);

        newNumberOfEvents = events.size();
        assertEquals(numberOfEvents + 1, newNumberOfEvents);
    }
}