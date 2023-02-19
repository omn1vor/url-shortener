package com.example.urlshortener.audit;

import com.example.urlshortener.UrlShortenerApplication;
import com.example.urlshortener.audit.event.UrlEventEntry;
import com.example.urlshortener.audit.event.UrlEventType;
import com.example.urlshortener.dto.CreateUrlRequest;
import com.example.urlshortener.dto.ShortenedUrlDto;
import com.example.urlshortener.service.ShortenedUrlService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.net.MalformedURLException;
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
    @Autowired
    CreateUrlRequest validUrlRequest;

    @Test
    public void testLogging() {
        CreateUrlRequest request = new CreateUrlRequest(validUrlRequest);
        request.setCode("");

        long numberOfEvents = urlEventService.findLast100().size();

        ShortenedUrlDto url = shortenedUrlService.addUrl(request);

        List<UrlEventEntry> events = urlEventService.findLast100();
        assertEquals(events.get(0).getType(), UrlEventType.CREATED);

        long newNumberOfEvents = events.size();
        assertEquals(numberOfEvents + 1, newNumberOfEvents);

        // Deactivating link
        url = shortenedUrlService.deactivate(url.getCode());
        numberOfEvents = newNumberOfEvents;

        events = urlEventService.findLast100();
        assertEquals(events.get(0).getType(), UrlEventType.DISABLED);

        newNumberOfEvents = events.size();
        assertEquals(numberOfEvents + 1, newNumberOfEvents);

        // Modifying link
        shortenedUrlService.activate(url.getCode());
        numberOfEvents = newNumberOfEvents;

        events = urlEventService.findLast100();
        assertEquals(events.get(0).getType(), UrlEventType.MODIFIED);

        newNumberOfEvents = events.size();
        assertEquals(numberOfEvents + 1, newNumberOfEvents);
    }
}