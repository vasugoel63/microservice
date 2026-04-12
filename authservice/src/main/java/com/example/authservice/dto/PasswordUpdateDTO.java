package com.example.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateDTO {
    private String email;
    private String oldPassword;
    private String newPassword;
}
