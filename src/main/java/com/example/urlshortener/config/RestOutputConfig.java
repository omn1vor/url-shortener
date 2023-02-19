package com.example.urlshortener.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.Min;

@Configuration
@ConfigurationProperties(prefix = "rest.output")
@Getter
public class RestOutputConfig {
    @Min(1)
    Integer maxClicksNumber = 100;
}
