package com.projectalpha.repository.user.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.model.user.UserProfileOwner;
import com.projectalpha.repository.user.UserProfileOwnerRepository;
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
public class SupabaseUserProfileOwnerRepositoryImpl implements UserProfileOwnerRepository {

    private static final String OWNERS_TABLE = "user_profile_owners";

    private final String supabaseUrl;
    private final String supabaseKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SupabaseUserProfileOwnerRepositoryImpl(
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
    public List<UserProfileOwner> findAll() {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + OWNERS_TABLE + "?select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<UserProfileOwner>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving owners", e);
        }
    }

    @Override
    public Optional<UserProfileOwner> findById(UUID id) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + OWNERS_TABLE + "?id=eq." + id + "&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            List<UserProfileOwner> owners = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileOwner>>(){});
            return owners.isEmpty() ? Optional.empty() : Optional.of(owners.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving owner by id", e);
        }
    }

    @Override
    public Optional<UserProfileOwner> findByEmail(String email) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + OWNERS_TABLE + "?email=eq." + email + "&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            List<UserProfileOwner> owners = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileOwner>>(){});
            return owners.isEmpty() ? Optional.empty() : Optional.of(owners.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving owner by email", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public Optional<UserProfileOwner> findBySupabaseUid(String supabaseUid) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + OWNERS_TABLE + "?supabase_uid=eq." + supabaseUid + "&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            List<UserProfileOwner> owners = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileOwner>>(){});
            return owners.isEmpty() ? Optional.empty() : Optional.of(owners.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving owner by supabase uid", e);
        }
    }

    @Override
    public Optional<UserProfileOwner> findActiveOwnerByEmail(String email) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + OWNERS_TABLE + "?email=eq." + email + "&active=eq.true&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            List<UserProfileOwner> owners = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileOwner>>(){});
            return owners.isEmpty() ? Optional.empty() : Optional.of(owners.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving active owner by email", e);
        }
    }

    @Override
    public Optional<UserProfileOwner> findByRoleName(String roleName) {
        // Bu metod biraz karmaşık, çünkü ilişkisel sorgular gerektirir
        // Supabase'in SQL API'sini veya stored procedure kullanabilirsiniz
        // Burada basitleştirilmiş bir yaklaşım:

        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/rpc/find_owner_by_role_name";

        HttpEntity<String> entity = new HttpEntity<>("{\"role_name\": \"" + roleName + "\"}", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);

        try {
            List<UserProfileOwner> owners = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileOwner>>(){});
            return owners.isEmpty() ? Optional.empty() : Optional.of(owners.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving owner by role name", e);
        }
    }

    @Override
    public Optional<UserProfileOwner> findByBusinessId(UUID businessId) {
        // Benzer şekilde, ilişkisel sorgu için stored procedure kullanın
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/rpc/find_owner_by_business_id";

        HttpEntity<String> entity = new HttpEntity<>("{\"business_id\": \"" + businessId + "\"}", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);

        try {
            List<UserProfileOwner> owners = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileOwner>>(){});
            return owners.isEmpty() ? Optional.empty() : Optional.of(owners.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving owner by business id", e);
        }
    }

    @Override
    public Optional<UserProfileOwner> findByPhoneNumber(String phoneNumber) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + OWNERS_TABLE + "?phone_number=eq." + phoneNumber + "&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            List<UserProfileOwner> owners = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileOwner>>(){});
            return owners.isEmpty() ? Optional.empty() : Optional.of(owners.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving owner by phone number", e);
        }
    }

    @Override
    public List<UserProfileOwner> findByCompanyNameContaining(String companyName) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + OWNERS_TABLE + "?company_name=ilike.%" + companyName + "%&select=*";

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        try {
            return objectMapper.readValue(
                    response.getBody(), new TypeReference<List<UserProfileOwner>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving owners by company name", e);
        }
    }

    @Override
    public UserProfileOwner save(UserProfileOwner owner) {
        HttpHeaders headers = createHeaders();
        headers.add("Prefer", "return=representation");

        String url = supabaseUrl + "/rest/v1/" + OWNERS_TABLE;

        try {
            String ownerJson = objectMapper.writeValueAsString(owner);
            HttpEntity<String> entity = new HttpEntity<>(ownerJson, headers);

            if (owner.getId() == null) {
                // Create new owner
                ResponseEntity<String> response = restTemplate.exchange(
                        url, HttpMethod.POST, entity, String.class);
                List<UserProfileOwner> owners = objectMapper.readValue(
                        response.getBody(), new TypeReference<List<UserProfileOwner>>(){});
                return owners.get(0);
            } else {
                // Update existing owner
                url = url + "?id=eq." + owner.getId();
                ResponseEntity<String> response = restTemplate.exchange(
                        url, HttpMethod.PATCH, entity, String.class);
                List<UserProfileOwner> owners = objectMapper.readValue(
                        response.getBody(), new TypeReference<List<UserProfileOwner>>(){});
                return owners.get(0);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving owner", e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        HttpHeaders headers = createHeaders();
        String url = supabaseUrl + "/rest/v1/" + OWNERS_TABLE + "?id=eq." + id;

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