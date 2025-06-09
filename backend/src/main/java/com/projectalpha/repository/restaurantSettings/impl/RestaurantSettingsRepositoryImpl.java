package com.projectalpha.repository.restaurantSettings.impl;

import com.projectalpha.repository.util.SupabaseHttpHelper;
import com.projectalpha.dto.business.restaurantsettings.RestaurantSettingsDTO;
import com.projectalpha.repository.restaurantSettings.RestaurantSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RestaurantSettingsRepositoryImpl implements RestaurantSettingsRepository {
    private final SupabaseHttpHelper httpHelper;

    @Autowired
    public RestaurantSettingsRepositoryImpl(SupabaseHttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    private List<RestaurantSettingsDTO> fetchList(String path) {
        return httpHelper.fetchList(path, RestaurantSettingsDTO.class);
    }

    private RestaurantSettingsDTO fetchSingle(String path) {
        return httpHelper.fetchSingle(path, RestaurantSettingsDTO.class);
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
