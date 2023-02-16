package com.example.urlshortener.service;

import com.example.urlshortener.model.ShortenedUrl;

public interface ClickTrackerService {
    void linkClicked(ShortenedUrl url, String ip);
}
