package com.example.urlshortener.service.impl;

import com.example.urlshortener.service.ShortUrlGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Profile("dev")
public class ShortUrlGeneratorTestImpl implements ShortUrlGenerator {
    @Override
    public String generate() {
        return "test-generator";
    }
}
