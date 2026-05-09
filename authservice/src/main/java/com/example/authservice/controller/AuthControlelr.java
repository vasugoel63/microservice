package com.example.authservice.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.dto.LoginRequestDTO;
import com.example.authservice.dto.LoginResponseDTO;
import com.example.authservice.dto.PasswordUpdateDTO;
import com.example.authservice.dto.UserRequestDTO;
import com.example.authservice.model.User;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.userService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/auth")
@RestController
public class AuthControlelr {
    private final AuthService AuthService;

    public AuthControlelr(AuthService AuthService) {
        this.AuthService = AuthService;
    }

    // @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        Optional<String> tokenOptional = AuthService.authenticate(loginRequestDTO);
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = tokenOptional.get();
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    // @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        System.out.println(token);

        return ResponseEntity.ok("Valid user");
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            User user = AuthService.createUser(userRequestDTO);
            return ResponseEntity.ok("User registered successfully" + user.getEmail());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        try {
            AuthService.updatePassword(passwordUpdateDTO);
            return ResponseEntity.ok("Password is updated");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
