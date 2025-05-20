package com.projectalpha.repository.user.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.model.user.UserProfileDiner;
import com.projectalpha.repository.user.UserProfileDinerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SupabaseUserProfileDinerRepositoryImpl implements UserProfileDinerRepository {

    private static final String DINERS_TABLE = "user_profile_diners";

    private final String supabaseUrl;
    private final String supabaseKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SupabaseUserProfileDinerRepositoryImpl(
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
    public List<UserProfileDiner> findAll() {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + DINERS_TABLE + "?select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<UserProfileDiner>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving diners", e);
        }
    }

    @Override
    public Optional<UserProfileDiner> findById(UUID id) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + DINERS_TABLE + "?id=eq." + id + "&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            List<UserProfileDiner> diners = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileDiner>>(){});
            return diners.isEmpty() ? Optional.empty() : Optional.of(diners.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving diner by id", e);
        }
    }

    @Override
    public Optional<UserProfileDiner> findByEmail(String email) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + DINERS_TABLE + "?email=eq." + email + "&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            List<UserProfileDiner> diners = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileDiner>>(){});
            return diners.isEmpty() ? Optional.empty() : Optional.of(diners.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving diner by email", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public Optional<UserProfileDiner> findBySupabaseUid(String supabaseUid) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + DINERS_TABLE + "?supabase_uid=eq." + supabaseUid + "&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            List<UserProfileDiner> diners = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileDiner>>(){});
            return diners.isEmpty() ? Optional.empty() : Optional.of(diners.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving diner by supabase uid", e);
        }
    }

    @Override
    public Optional<UserProfileDiner> findActiveDinerByEmail(String email) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + DINERS_TABLE + "?email=eq." + email + "&active=eq.true&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            List<UserProfileDiner> diners = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileDiner>>(){});
            return diners.isEmpty() ? Optional.empty() : Optional.of(diners.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving active diner by email", e);
        }
    }

    @Override
    public List<UserProfileDiner> findByNameContaining(String name) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + DINERS_TABLE + "?name=ilike.%" + name + "%&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            return objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileDiner>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving diners by name", e);
        }
    }

    @Override
    public UserProfileDiner save(UserProfileDiner diner) {
        HttpHeaders headers = createHeaders();
        headers.add("Prefer", "return=representation");

        String url = supabaseUrl + "/rest/v1/" + DINERS_TABLE;

        try {
            String dinerJson = objectMapper.writeValueAsString(diner);
            HttpEntity<String> entity = new HttpEntity<>(dinerJson, headers);

            if (diner.getId() == null) {
                // Create new diner
                ResponseEntity<String> response = restTemplate.exchange(
                        url, HttpMethod.POST, entity, String.class);
                List<UserProfileDiner> diners = objectMapper.readValue(
                        response.getBody(), new TypeReference<List<UserProfileDiner>>(){});
                return diners.get(0);
            } else {
                // Update existing diner
                url = url + "?id=eq." + diner.getId();
                ResponseEntity<String> response = restTemplate.exchange(
                        url, HttpMethod.PATCH, entity, String.class);
                List<UserProfileDiner> diners = objectMapper.readValue(
                        response.getBody(), new TypeReference<List<UserProfileDiner>>(){});
                return diners.get(0);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving diner", e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + DINERS_TABLE + "?id=eq." + id;

        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        return headers;
    }
}