package com.example.urlshortener.service;

import com.example.urlshortener.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
}
