package com.projectalpha.repository.user.owner.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.projectalpha.dto.business.BusinessDTO;
import com.projectalpha.dto.business.address.AddressDTO;
import com.projectalpha.dto.user.owner.OwnerRegisterRequest;
import com.projectalpha.dto.user.owner.OwnerUserProfile;
import com.projectalpha.repository.user.owner.OwnerRepository;
import com.projectalpha.config.thirdparty.SupabaseConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

@Repository
public class OwnerRepositoryImpl implements OwnerRepository {

    private final SupabaseConfig supabaseConfig;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public OwnerRepositoryImpl(SupabaseConfig supabaseConfig) {
        this.supabaseConfig = supabaseConfig;
    }

    @Override
    public Optional<OwnerUserProfile> findOwnerByID(String userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(supabaseConfig.getSupabaseUrl() + "/rest/v1/user_profile_owner?user_id=eq." + userId))
                    .header("apikey", supabaseConfig.getSupabaseApiKey())
                    .header("Authorization", "Bearer " + supabaseConfig.getSupabaseSecretKey())
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                if (root.isArray() && root.size() > 0) {
                    OwnerUserProfile profile = mapper.treeToValue(root.get(0), OwnerUserProfile.class);
                    return Optional.of(profile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void updateOwnerProfile(String userId, OwnerUserProfile profile) {
        try {
            String body = mapper.writeValueAsString(new OwnerUpdateDTO(profile));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(supabaseConfig.getSupabaseUrl() + "/rest/v1/user_profile_owner?user_id=eq." + userId))
                    .header("apikey", supabaseConfig.getSupabaseApiKey())
                    .header("Authorization", "Bearer " + supabaseConfig.getSupabaseSecretKey())
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new RuntimeException("Update failed: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class OwnerUpdateDTO {
        public String name;
        public String phone_numb;
        public String surname;

        public OwnerUpdateDTO(OwnerUserProfile profile) {
            this.name = profile.getName();
            this.phone_numb = profile.getPhone_numb();
            this.surname = profile.getSurname();
        }
    }
    @Override
    public BusinessDTO saveBusiness(String ownerId, BusinessDTO business) {
        try {
            // JSON formatına çevir
            String body = mapper.writeValueAsString(business);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(supabaseConfig.getSupabaseUrl() + "/rest/v1/business"))
                    .header("apikey", supabaseConfig.getSupabaseApiKey())
                    .header("Authorization", "Bearer " + supabaseConfig.getSupabaseSecretKey())
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=representation") // Supabase'in geri dönüş olarak objeyi dönmesini sağlıyor
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                JsonNode root = mapper.readTree(response.body());
                if (root.isArray() && root.size() > 0) {
                    return mapper.treeToValue(root.get(0), BusinessDTO.class);
                }
            } else {
                throw new RuntimeException("Failed to save business: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving business", e);
        }
        return null;
    }
    @Override
    public AddressDTO saveAddress(String businessId, AddressDTO address) {
        try {
            String body = mapper.writeValueAsString(address);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(supabaseConfig.getSupabaseUrl() + "/rest/v1/addresses"))
                    .header("apikey", supabaseConfig.getSupabaseApiKey())
                    .header("Authorization", "Bearer " + supabaseConfig.getSupabaseSecretKey())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                JsonNode root = mapper.readTree(response.body());
                if (root.isArray() && root.size() > 0) {
                    return mapper.treeToValue(root.get(0), AddressDTO.class);
                }
            } else {
                throw new RuntimeException("Failed to save address: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving address", e);
        }
        return null;
    }
    @Override
    public BusinessDTO createInitialBusinessForOwner(String ownerId, OwnerRegisterRequest request) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        // 1. Business oluştur (POST)
        Map<String, Object> businessPayload = Map.of(
                "name", request.getRequestBusiness().getName(),
                "description", request.getRequestBusiness().getDescription(),
                "owner_id1", ownerId
        );
        String businessJson = mapper.writeValueAsString(new Map[]{businessPayload});

        HttpRequest businessRequest = HttpRequest.newBuilder()
                .uri(URI.create(supabaseConfig.getSupabaseUrl() + "/rest/v1/business"))
                .header("apikey", supabaseConfig.getSupabaseApiKey())
                .header("Authorization", "Bearer " + supabaseConfig.getSupabaseSecretKey())
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .POST(HttpRequest.BodyPublishers.ofString(businessJson))
                .build();
        HttpResponse<String> businessResponse = httpClient.send(businessRequest, HttpResponse.BodyHandlers.ofString());

        BusinessDTO[] businesses = mapper.readValue(businessResponse.body(), BusinessDTO[].class);
        BusinessDTO createdBusiness = businesses[0];
        String businessId = createdBusiness.getId();
        // 2. Address oluştur (POST)
        Map<String, Object> addressPayload = Map.of(
                "city", request.getRequestAddress().getCity(),
                "district", request.getRequestAddress().getDistrict(),
                "neighborhood", request.getRequestAddress().getNeighborhood(),
                "business_id", businessId
        );
        String addressJson = mapper.writeValueAsString(new Map[]{addressPayload});

        HttpRequest addressRequest = HttpRequest.newBuilder()
                .uri(URI.create(supabaseConfig.getSupabaseUrl() + "/rest/v1/addresses"))
                .header("apikey", supabaseConfig.getSupabaseApiKey())
                .header("Authorization", "Bearer " + supabaseConfig.getSupabaseSecretKey())
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .POST(HttpRequest.BodyPublishers.ofString(addressJson))
                .build();

        HttpResponse<String> addressResponse = httpClient.send(addressRequest, HttpResponse.BodyHandlers.ofString());

        AddressDTO[] addresses = mapper.readValue(addressResponse.body(), AddressDTO[].class);
        AddressDTO createdAddress = addresses[0];
        String addressId = createdAddress.getId();
        // 3. Business'a addressId ekle (PATCH)
        Map<String, Object> updateBusinessPayload = Map.of(
                "addresses", addressId
        );

        String updateBusinessJson = mapper.writeValueAsString(updateBusinessPayload);

        HttpRequest updateBusinessRequest = HttpRequest.newBuilder()
                .uri(URI.create(supabaseConfig.getSupabaseUrl() + "/rest/v1/business?id=eq." + businessId))
                .header("apikey", supabaseConfig.getSupabaseApiKey())
                .header("Authorization", "Bearer " + supabaseConfig.getSupabaseSecretKey())
                .header("Content-Type", "application/json")
                .header("Prefer", "return=minimal")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(updateBusinessJson))
                .build();

        httpClient.send(updateBusinessRequest, HttpResponse.BodyHandlers.discarding());



        // 4. Owner profil güncelle (PATCH)
        Map<String, Object> profilePayload = Map.of(
                "name", request.getRequestOwner().getName(),
                "surname", request.getRequestOwner().getSurname(),
                "phone_numb", request.getRequestOwner().getPhone_numb()
        );

        String profileJson = mapper.writeValueAsString(profilePayload);

        //owner_id ile satırı bul. ilgili kolonun bilgilerini değiştir.
        String column = "id";
        String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/user_profile_owner?select=" + column + "&owner_id=eq." + ownerId;

        HttpRequest profileRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", supabaseConfig.getSupabaseApiKey())
                .header("Authorization", "Bearer " + supabaseConfig.getSupabaseSecretKey())
                .header("Content-Type", "application/json")
                .header("Prefer", "return=minimal")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(profileJson))
                .build();

        httpClient.send(profileRequest, HttpResponse.BodyHandlers.discarding());

         //Son olarak güncellenmiş business objesini döndür
        createdBusiness.setAddress(addressId);
        return createdBusiness;
    }
}