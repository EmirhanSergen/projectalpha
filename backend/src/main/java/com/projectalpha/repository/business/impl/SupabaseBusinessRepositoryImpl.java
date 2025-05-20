package com.projectalpha.repository.business.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.model.business.Business;
import com.projectalpha.repository.business.BusinessRepository;
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
public class SupabaseBusinessRepositoryImpl implements BusinessRepository {

    private static final String BUSINESSES_TABLE = "businesses";

    private final String supabaseUrl;
    private final String supabaseKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SupabaseBusinessRepositoryImpl(
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
    public List<Business> findAll() {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + BUSINESSES_TABLE + "?select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<Business>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving businesses", e);
        }
    }

    @Override
    public Optional<Business> findById(UUID id) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + BUSINESSES_TABLE + "?id=eq." + id + "&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            List<Business> businesses = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<Business>>(){});
            return businesses.isEmpty() ? Optional.empty() : Optional.of(businesses.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving business by id", e);
        }
    }

    @Override
    public Optional<Business> findByName(String name) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + BUSINESSES_TABLE + "?name=eq." + name + "&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            List<Business> businesses = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<Business>>(){});
            return businesses.isEmpty() ? Optional.empty() : Optional.of(businesses.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving business by name", e);
        }
    }

    @Override
    public List<Business> findByNameContaining(String name) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + BUSINESSES_TABLE + "?name=ilike.%" + name + "%&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            return objectMapper.readValue(
                    response.getBody(), new TypeReference<List<Business>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving businesses by name", e);
        }
    }

    @Override
    public List<Business> findByLocation(String location) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + BUSINESSES_TABLE + "?location=eq." + location + "&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            return objectMapper.readValue(
                    response.getBody(), new TypeReference<List<Business>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving businesses by location", e);
        }
    }

    @Override
    public List<Business> findByCategory(String category) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + BUSINESSES_TABLE + "?category=eq." + category + "&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            return objectMapper.readValue(
                    response.getBody(), new TypeReference<List<Business>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving businesses by category", e);
        }
    }

    @Override
    public Business save(Business business) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        return headers;
    }
}