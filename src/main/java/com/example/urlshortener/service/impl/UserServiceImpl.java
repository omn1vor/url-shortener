package com.example.urlshortener.service.impl;

import com.example.urlshortener.dto.UserDto;
import com.example.urlshortener.model.User;
import com.example.urlshortener.service.UserService;
import com.example.urlshortener.service.storage.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAllByOrderByEmail();
    }
}
