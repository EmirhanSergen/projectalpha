package com.projectalpha.repository.business;

import com.projectalpha.dto.business.BusinessDTO;
import com.projectalpha.dto.business.address.AddressDTO;
import com.projectalpha.dto.user.owner.OwnerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

@Repository
public class SupabaseBusinessRepository implements BusinessRepository {

    private final String supabaseUrl;
    private final String supabaseKey;
    private final RestTemplate restTemplate;

    public SupabaseBusinessRepository(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.apiKey}") String supabaseKey,
            RestTemplate restTemplate) {
        this.supabaseUrl = supabaseUrl;
        this.supabaseKey = supabaseKey;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<BusinessDTO> findAll() {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<BusinessDTO[]> response = restTemplate.exchange(
                supabaseUrl + "/rest/v1/business?select=*",
                HttpMethod.GET,
                entity,
                BusinessDTO[].class
        );

        List<BusinessDTO> businesses = Arrays.asList(response.getBody());
        return enrichBusinessesWithDetails(businesses);
    }

    @Override
    public Optional<BusinessDTO> findById(UUID id) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<BusinessDTO[]> response = restTemplate.exchange(
                supabaseUrl + "/rest/v1/business?id=eq." + id + "&select=*",
                HttpMethod.GET,
                entity,
                BusinessDTO[].class
        );

        BusinessDTO[] businesses = response.getBody();
        if (businesses != null && businesses.length > 0) {
            BusinessDTO business = businesses[0];
            return Optional.of(enrichBusinessWithDetails(business));
        }
        return Optional.empty();
    }

    @Override
    public List<BusinessDTO> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAll();
        }

        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Supabase'in full-text search özelliğini kullanıyoruz
        ResponseEntity<BusinessDTO[]> response = restTemplate.exchange(
                supabaseUrl + "/rest/v1/business?or=(name.ilike.%" + query + "%,description.ilike.%" + query + "%)",
                HttpMethod.GET,
                entity,
                BusinessDTO[].class
        );

        List<BusinessDTO> businesses = Arrays.asList(response.getBody());
        return enrichBusinessesWithDetails(businesses);
    }

    // İşletmelere adres ve sahip bilgilerini ekleyen yardımcı metod
    private List<BusinessDTO> enrichBusinessesWithDetails(List<BusinessDTO> businesses) {
        return businesses.stream()
                .map(this::enrichBusinessWithDetails)
                .toList();
    }

    // İşletmeye adres ve sahip bilgilerini ekleyen yardımcı metod
    private BusinessDTO enrichBusinessWithDetails(BusinessDTO business) {
        // Adres bilgisini getir ve ekle
        if (business.getAddressId() != null) {
            getAddressForBusiness(business.getAddressId()).ifPresent(business::setAddress);
        }

        // Sahip bilgisini getir ve ekle
        if (business.getOwnerId() != null) {
            getOwnerForBusiness(business.getOwnerId()).ifPresent(business::setOwner);
        }

        return business;
    }

    // İşletmenin adres bilgisini getiren yardımcı metod
    private Optional<AddressDTO> getAddressForBusiness(UUID addressId) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<AddressDTO[]> response = restTemplate.exchange(
                    supabaseUrl + "/rest/v1/address?id=eq." + addressId + "&select=*",
                    HttpMethod.GET,
                    entity,
                    AddressDTO[].class
            );

            AddressDTO[] addresses = response.getBody();
            if (addresses != null && addresses.length > 0) {
                return Optional.of(addresses[0]);
            }
        } catch (Exception e) {
            // Hata günlüğe kaydedilebilir
        }
        return Optional.empty();
    }

    // İşletmenin sahip bilgisini getiren yardımcı metod
    private Optional<OwnerDTO> getOwnerForBusiness(UUID ownerId) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<OwnerDTO[]> response = restTemplate.exchange(
                    supabaseUrl + "/rest/v1/owner?id=eq." + ownerId + "&select=*",
                    HttpMethod.GET,
                    entity,
                    OwnerDTO[].class
            );

            OwnerDTO[] owners = response.getBody();
            if (owners != null && owners.length > 0) {
                return Optional.of(owners[0]);
            }
        } catch (Exception e) {
            // Hata günlüğe kaydedilebilir
        }
        return Optional.empty();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}