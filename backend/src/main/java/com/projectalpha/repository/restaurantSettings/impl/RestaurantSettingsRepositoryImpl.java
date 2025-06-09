package com.projectalpha.repository.restaurantSettings.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.config.thirdparty.SupabaseConfig;
import com.projectalpha.dto.business.restaurantsettings.RestaurantSettingsDTO;
import com.projectalpha.repository.restaurantSettings.RestaurantSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import com.projectalpha.util.SupabaseHttpHelper;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantSettingsRepositoryImpl implements RestaurantSettingsRepository {
    private final SupabaseConfig supabaseConfig;
    private final SupabaseHttpHelper httpHelper;
    private final ObjectMapper objectMapper;

    @Autowired
    public RestaurantSettingsRepositoryImpl(SupabaseConfig supabaseConfig, SupabaseHttpHelper httpHelper) {
        this.supabaseConfig = supabaseConfig;
        this.httpHelper = httpHelper;
        this.objectMapper = new ObjectMapper();
    }

    private List<RestaurantSettingsDTO> fetchList(String path) {
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/" + path;
            String body = httpHelper.get(url);

            JsonNode root = objectMapper.readTree(body);
            List<RestaurantSettingsDTO> list = new ArrayList<>();
            for (JsonNode node : root) {
                list.add(objectMapper.treeToValue(node, RestaurantSettingsDTO.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching " + path, e);
        }
    }

    private RestaurantSettingsDTO fetchSingle(String path) {
        List<RestaurantSettingsDTO> list = fetchList(path);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<RestaurantSettingsDTO> findAll() {
        return fetchList("restaurant_settings?select=*");
    }

    @Override
    public RestaurantSettingsDTO findById(String id) {
        return fetchSingle("restaurant_settings?select=*&id=eq." + id);
    }

    @Override
    public RestaurantSettingsDTO findByBusinessId(String businessId) {
        return fetchSingle("restaurant_settings?select=*&business_id=eq." + businessId);
    }
}
