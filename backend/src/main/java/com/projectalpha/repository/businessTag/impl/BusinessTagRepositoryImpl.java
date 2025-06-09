package com.projectalpha.repository.businessTag.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.config.thirdparty.SupabaseConfig;
import com.projectalpha.dto.business.businessTag.BusinessTagDTO;
import com.projectalpha.repository.businessTag.BusinessTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import com.projectalpha.util.SupabaseHttpHelper;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BusinessTagRepositoryImpl implements BusinessTagRepository {
    private final SupabaseConfig supabaseConfig;
    private final SupabaseHttpHelper httpHelper;
    private final ObjectMapper objectMapper;

    @Autowired
    public BusinessTagRepositoryImpl(SupabaseConfig supabaseConfig, SupabaseHttpHelper httpHelper) {
        this.supabaseConfig = supabaseConfig;
        this.httpHelper = httpHelper;
        this.objectMapper = new ObjectMapper();
    }

    private List<BusinessTagDTO> fetchList(String path) {
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/" + path;
            String body = httpHelper.get(url);

            JsonNode root = objectMapper.readTree(body);
            List<BusinessTagDTO> list = new ArrayList<>();
            for (JsonNode n : root) list.add(objectMapper.treeToValue(n, BusinessTagDTO.class));
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching " + path, e);
        }
    }

    private BusinessTagDTO fetchSingle(String path) {
        List<BusinessTagDTO> list = fetchList(path);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<BusinessTagDTO> findAll() {
        return fetchList("business_tag?select=*");
    }

    @Override
    public BusinessTagDTO findById(String id) {
        return fetchSingle("business_tag?select=*&id=eq." + id);
    }

    @Override
    public List<BusinessTagDTO> findByBusinessId(String businessId) {
        return fetchList("business_tag?select=*&business_id=eq." + businessId);
    }

    @Override
    public List<BusinessTagDTO> findByTagId(String tagId) {
        return fetchList("business_tag?select=*&tag_id=eq." + tagId);
    }
}