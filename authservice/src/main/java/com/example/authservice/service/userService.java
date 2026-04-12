package com.example.authservice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;

@Service
public class userService {

    private final UserRepository userRepository;

    public userService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
