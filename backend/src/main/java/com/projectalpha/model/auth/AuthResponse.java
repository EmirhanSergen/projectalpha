package com.projectalpha.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String accessToken;

    private String refreshToken;

    private String userId;

    private String email;

    private String role;

    private LocalDateTime expiresAt;

    private boolean success;

    private String message;
}