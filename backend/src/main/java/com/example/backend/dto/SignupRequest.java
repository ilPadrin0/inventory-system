package com.example.backend.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
    private boolean admin;  // true 면 ROLE_ADMIN, false 면 ROLE_USER
}
