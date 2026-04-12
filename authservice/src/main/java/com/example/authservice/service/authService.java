package com.example.authservice.service;

import java.lang.foreign.Linker.Option;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.authservice.dto.LoginRequestDTO;
import com.example.authservice.dto.PasswordUpdateDTO;
import com.example.authservice.dto.UserRequestDTO;
import com.example.authservice.model.User;
import com.example.authservice.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.jsonwebtoken.JwtException;

@Service
public class authService {

    private final userService UserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public authService(userService UserService, PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.UserService = UserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        Optional<String> token = UserService.findByEmail(loginRequestDTO.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(), u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));

        return token;
    }

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public User createUser(UserRequestDTO userRequestDTO) {
        if (UserService.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with " + userRequestDTO.getEmail());
        }
        User user = new User();
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword())); // Encode password
        user.setRole(userRequestDTO.getRole()); // e.g., "USER" or "ADMIN"

        // Save the user using userService
        return UserService.save(user);
    }

    public boolean updatePassword(PasswordUpdateDTO passwordUpdateDTO) {
        Optional<User> optionalUser = UserService.findByEmail(passwordUpdateDTO.getEmail());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        if (!passwordEncoder.matches(passwordUpdateDTO.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is required");
        }
        user.setPassword(passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));
        UserService.save(user);
        return true;
    }

}
