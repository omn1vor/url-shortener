package com.example.urlshortener.service;

import com.example.urlshortener.dto.ClickEntryDto;

import java.util.List;

public interface ClickTrackerService {
    void linkClicked(String code, String ip);

    List<ClickEntryDto> getClicks(String code, Integer limit);
}
