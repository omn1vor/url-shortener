package com.example.urlshortener.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.Min;

// It's over-engineered for the sake of leaving an example for myself
// Should have probably just used Max(100) validation on the controller param
@Configuration
@ConfigurationProperties(prefix = "rest.output")
@Getter @Setter
public class RestOutputConfig {
    @Min(1)
    Integer maxClicksNumber = 100;
}
