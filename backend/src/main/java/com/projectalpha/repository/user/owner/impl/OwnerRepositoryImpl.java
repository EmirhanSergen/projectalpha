package com.projectalpha.repository.user.owner.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.projectalpha.dto.business.Business;
import com.projectalpha.dto.business.BusinessDTO;
import com.projectalpha.dto.business.address.AddressDTO;
import com.projectalpha.dto.review.ReviewSupabase;
import com.projectalpha.dto.user.diner.DinerUserProfile;
import com.projectalpha.dto.user.owner.OwnerLoginResponse;
import com.projectalpha.dto.user.owner.OwnerRegisterRequest;
import com.projectalpha.dto.user.owner.OwnerUpdateRequest;
import com.projectalpha.dto.user.owner.OwnerUserProfile;
import com.projectalpha.exception.auth.UserNotFoundException;
import com.projectalpha.repository.user.owner.OwnerRepository;
import com.projectalpha.config.thirdparty.SupabaseConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import com.projectalpha.util.SupabaseHttpHelper;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class OwnerRepositoryImpl implements OwnerRepository {

    private final SupabaseConfig supabaseConfig;
    private final SupabaseHttpHelper httpHelper;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public OwnerRepositoryImpl(SupabaseConfig supabaseConfig, SupabaseHttpHelper httpHelper) {
        this.supabaseConfig = supabaseConfig;
        this.httpHelper = httpHelper;
    }

    @Override
    public Optional<OwnerLoginResponse> findOwnerByID(String userId, List<ReviewSupabase> businessReviews) {
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/user_profile_owner?owner_id=eq." + userId;
            String resp = httpHelper.get(url);

            JsonNode root = mapper.readTree(resp);
            if (root.isArray() && root.size() > 0) {
                    OwnerUserProfile profile = mapper.treeToValue(root.get(0), OwnerUserProfile.class);
                    Business businessProfile = getBusinessProfile(userId);

                    OwnerLoginResponse responseProfile = new OwnerLoginResponse(profile, businessProfile, businessReviews);

                    return Optional.of(responseProfile) ;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Business getBusinessProfile(String ownerId){
        try {
            String businessUrl = supabaseConfig.getSupabaseUrl() +
                      "/rest/v1/business?select=*" + "&owner_id1=eq." + ownerId;

            String businessResp = httpHelper.get(businessUrl);
            Business[] businesses = mapper.readValue(businessResp, Business[].class);
            return businesses[0];



        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateOwnerProfile(String userId, OwnerUpdateRequest request) {
        try {
            Map<String, Object> profilePayload = Map.of(
                    "name", request.getRequestOwner().getName(),
                    "surname", request.getRequestOwner().getSurname(),
                    "phone_numb", request.getRequestOwner().getPhone_numb()
            );

            String profileJson = mapper.writeValueAsString(profilePayload);

            //user_id ile satırı bul. ilgili kolonun bilgilerini değiştir.
            String column = "id";
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/user_profile_owner?select=" + column + "&owner_id=eq." + userId;

            httpHelper.patch(url, profileJson, "return=minimal");
        } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    @Override
    public BusinessDTO saveBusiness(String ownerId, BusinessDTO business) {
        try {
            // JSON formatına çevir
            String body = mapper.writeValueAsString(business);

            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/business";
            String resp = httpHelper.post(url, body, "return=representation");

            JsonNode root = mapper.readTree(resp);
            if (root.isArray() && root.size() > 0) {
                    return mapper.treeToValue(root.get(0), BusinessDTO.class);
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

            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/addresses";
            String resp = httpHelper.post(url, body, "return=representation");
            JsonNode root = mapper.readTree(resp);
                if (root.isArray() && root.size() > 0) {
                    return mapper.treeToValue(root.get(0), AddressDTO.class);
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

        String businessUrl = supabaseConfig.getSupabaseUrl() + "/rest/v1/business";
        String businessResp = httpHelper.post(businessUrl, businessJson, "return=representation");

        BusinessDTO[] businesses = mapper.readValue(businessResp, BusinessDTO[].class);
        BusinessDTO createdBusiness = businesses[0];
        String businessId = createdBusiness.getId();
        // 2. Address oluştur (POST)
        Map<String, Object> addressPayload = Map.of(
                "city", request.getRequestAddress().getCity(),
                "district", request.getRequestAddress().getDistrict(),
                "neighborhood", request.getRequestAddress().getNeighborhood(),
                "street", request.getRequestAddress().getStreet(),
                "business_id", businessId
        );
        String addressJson = mapper.writeValueAsString(new Map[]{addressPayload});

        String addressUrl = supabaseConfig.getSupabaseUrl() + "/rest/v1/addresses";
        String addressResp = httpHelper.post(addressUrl, addressJson, "return=representation");

        AddressDTO[] addresses = mapper.readValue(addressResp, AddressDTO[].class);
        AddressDTO createdAddress = addresses[0];
        String addressId = createdAddress.getId();
        // 3. Business'a addressId ekle (PATCH)
        Map<String, Object> updateBusinessPayload = Map.of(
                "addresses", addressId
        );

        String updateBusinessJson = mapper.writeValueAsString(updateBusinessPayload);

        String updateUrl = supabaseConfig.getSupabaseUrl() + "/rest/v1/business?id=eq." + businessId;
        httpHelper.patch(updateUrl, updateBusinessJson, "return=minimal");



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

        httpHelper.patch(url, profileJson, "return=minimal");

         //Son olarak güncellenmiş business objesini döndür
        createdBusiness.setAddress(addressId);
        return createdBusiness;
    }
}