package com.projectalpha.repository.address.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.config.thirdparty.SupabaseConfig;
import com.projectalpha.dto.business.address.AddressDTO;
import com.projectalpha.repository.address.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import com.projectalpha.util.SupabaseHttpHelper;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

    private final SupabaseConfig supabaseConfig;
    private final SupabaseHttpHelper httpHelper;
    private final ObjectMapper objectMapper;

    @Autowired
    public AddressRepositoryImpl(SupabaseConfig supabaseConfig, SupabaseHttpHelper httpHelper) {
        this.supabaseConfig = supabaseConfig;
        this.httpHelper = httpHelper;
        this.objectMapper = new ObjectMapper();
    }

    private List<AddressDTO> fetchList(String path) {
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/" + path;
            String body = httpHelper.get(url);

            JsonNode root = objectMapper.readTree(body);
            List<AddressDTO> list = new ArrayList<>();
            for (JsonNode node : root) {
                list.add(objectMapper.treeToValue(node, AddressDTO.class));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching " + path, e);
        }
    }

    private AddressDTO fetchSingle(String path) {
        List<AddressDTO> list = fetchList(path);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<AddressDTO> findAll() {
        return fetchList("addresses?select=*");
    }

    @Override
    public AddressDTO findById(String id) {
        return fetchSingle("addresses?select=*&id=eq." + id);
    }

    @Override
    public AddressDTO findByBusinessId(String businessId) {
        return fetchSingle("addresses?select=*&business_id=eq." + businessId);
    }
}