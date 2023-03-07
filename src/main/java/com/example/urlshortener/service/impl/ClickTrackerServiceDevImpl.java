package com.example.urlshortener.service.impl;

import com.example.urlshortener.service.ClickTrackerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!prod")
@Slf4j
public class ClickTrackerServiceDevImpl implements ClickTrackerService {
    @Override
    public void linkClicked(String code, String ip) {
        log.info("Link '{}' clicked from ip {}", code, ip);
    }
}
