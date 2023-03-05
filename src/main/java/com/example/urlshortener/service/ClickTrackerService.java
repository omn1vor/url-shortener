package com.example.urlshortener.service;

public interface ClickTrackerService {
    void linkClicked(String code, String ip);
}
