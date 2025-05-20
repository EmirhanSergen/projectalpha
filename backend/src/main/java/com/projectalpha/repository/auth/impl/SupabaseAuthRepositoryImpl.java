package com.projectalpha.repository.auth.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.dto.auth.request.AuthRequestDto;
import com.projectalpha.dto.auth.response.AuthResponseDto;
import com.projectalpha.dto.auth.UserDto;
import com.projectalpha.dto.thirdparty.SupabaseTokenResponse;
import com.projectalpha.repository.auth.AuthRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class SupabaseAuthRepositoryImpl implements AuthRepository {

    private final String supabaseUrl;
    private final String supabaseKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SupabaseAuthRepositoryImpl(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.apiKey}") String supabaseKey,
            RestTemplate restTemplate,
            ObjectMapper objectMapper) {
        this.supabaseUrl = supabaseUrl;
        this.supabaseKey = supabaseKey;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthResponseDto signUp(AuthRequestDto authRequest) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/auth/v1/signup";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", authRequest.getEmail());
        requestBody.put("password", authRequest.getPassword());

        try {
            String requestJson = objectMapper.writeValueAsString(requestBody);
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class);

            return objectMapper.readValue(response.getBody(), AuthResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error during signup", e);
        }
    }

    @Override
    public AuthResponseDto signIn(AuthRequestDto authRequest) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/auth/v1/token?grant_type=password";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", authRequest.getEmail());
        requestBody.put("password", authRequest.getPassword());

        try {
            String requestJson = objectMapper.writeValueAsString(requestBody);
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class);

            return objectMapper.readValue(response.getBody(), AuthResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error during signin", e);
        }
    }

    @Override
    public void signOut(String accessToken) {
        HttpHeaders headers = createHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        String url = supabaseUrl + "/auth/v1/logout";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
    }

    @Override
    public Optional<UserDto> getCurrentUser(String accessToken) {
        HttpHeaders headers = createHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        String url = supabaseUrl + "/auth/v1/user";

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);

            UserDto user = objectMapper.readValue(response.getBody(), UserDto.class);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void resetPassword(String email) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/auth/v1/recover";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);

        try {
            String requestJson = objectMapper.writeValueAsString(requestBody);
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Error during password reset", e);
        }
    }

    @Override
    public AuthResponseDto refreshToken(String refreshToken) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/auth/v1/token?grant_type=refresh_token";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("refresh_token", refreshToken);

        try {
            String requestJson = objectMapper.writeValueAsString(requestBody);
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class);

            return objectMapper.readValue(response.getBody(), AuthResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error during token refresh", e);
        }
    }

    @Override
    public void sendVerificationCode(String email) {

    }

    @Override
    public SupabaseTokenResponse verifyToken(String email, String token) {
        return null;
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        // Örneğin Supabase veya başka bir dış API'ye istek gönder
        // ya da yerel bir veritabanı güncellemesi yap
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseKey);
        return headers;
    }
}