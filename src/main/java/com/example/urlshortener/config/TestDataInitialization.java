package com.example.urlshortener.config;


import com.example.urlshortener.dto.CreateUrlRequest;
import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.MalformedURLException;

@Configuration
@Profile("dev")
public class TestDataInitialization {

    private final String email = "luna@thatschool.com";
    private final String address = "https://www.google.com/";

    @Bean(name = "validUser")
    User validUser() {
        return new User(email);
    }

    @Bean(name = "validUrlRequest")
    CreateUrlRequest validRequest() {
        CreateUrlRequest request = new CreateUrlRequest();
        request.setCode("luna");
        request.setUrl(address);
        request.setEmail(email);
        return request;
    }

    @Bean(name = "validShortenedUrl")
    ShortenedUrl validShortenedUrl(@Autowired User validUser) {
        ShortenedUrl url = new ShortenedUrl();
        url.setCode("luna");
        url.setAuthor(validUser);
        url.setUrl(address);
        return url;
    }
}
