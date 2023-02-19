package com.example.urlshortener.service.impl;

import com.example.urlshortener.config.RestOutputConfig;
import com.example.urlshortener.dto.ClickEntryDto;
import com.example.urlshortener.model.ClickEntry;
import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.service.ClickTrackerService;
import com.example.urlshortener.service.ExceptionBuilder;
import com.example.urlshortener.service.storage.ClickEntryRepository;
import com.example.urlshortener.service.storage.ShortenedUrlRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClickTrackerServiceImpl implements ClickTrackerService {
    @Autowired
    private ClickEntryRepository clickEntryRepository;
    @Autowired
    private ShortenedUrlRepository shortenedUrlRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    ExceptionBuilder exceptionBuilder;
    @Autowired
    RestOutputConfig restOutputConfig;

    @Override
    public void linkClicked(String code, String ip) {
        Optional<ShortenedUrl> foundUrl = shortenedUrlRepository.findByCode(code);
        if (foundUrl.isEmpty()) {
            return;
        }

        ClickEntry clickEntry = new ClickEntry();
        clickEntry.setUrl(foundUrl.get());
        clickEntry.setIp(ip);
        clickEntry.setDate(LocalDateTime.now());
        clickEntryRepository.save(clickEntry);
    }

    @Override
    public List<ClickEntryDto> getClicks(String code, Integer userLimit) {
        int limit = userLimit == null ? 10 : userLimit;
        if (limit > restOutputConfig.getMaxClicksNumber()) {
            throw exceptionBuilder.outputLimitExceeded(restOutputConfig.getMaxClicksNumber());
        }
        Pageable pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Order.desc("date")));
        return clickEntryRepository.findAllByUrl_Code(code, pageRequest)
                .map(this::convertToDto)
                .getContent();
    }

    private ClickEntryDto convertToDto(ClickEntry clickEntry) {
        return modelMapper.map(clickEntry, ClickEntryDto.class);
    }
}
