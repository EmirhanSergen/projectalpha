package com.projectalpha.repository.tag.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.config.thirdparty.SupabaseConfig;
import com.projectalpha.dto.business.tag.TagDTO;
import com.projectalpha.repository.tag.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import com.projectalpha.util.SupabaseHttpHelper;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private final SupabaseConfig supabaseConfig;
    private final SupabaseHttpHelper httpHelper;
    private final ObjectMapper objectMapper;

    @Autowired
    public TagRepositoryImpl(SupabaseConfig supabaseConfig, SupabaseHttpHelper httpHelper) {
        this.supabaseConfig = supabaseConfig;
        this.httpHelper = httpHelper;
        this.objectMapper = new ObjectMapper();
    }

    private List<TagDTO> fetchList(String path) {
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/" + path;
            String body = httpHelper.get(url);

            JsonNode root = objectMapper.readTree(body);
            List<TagDTO> list = new ArrayList<>();
            for (JsonNode n : root) list.add(objectMapper.treeToValue(n, TagDTO.class));
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching " + path, e);
        }
    }

    private TagDTO fetchSingle(String path) {
        List<TagDTO> list = fetchList(path);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<TagDTO> findAll() {
        return fetchList("tag?select=*");
    }

    @Override
    public TagDTO findById(String id) {
        return fetchSingle("tag?select=*&id=eq." + id);
    }

    @Override
    public List<TagDTO> findByNameContainingIgnoreCase(String name) {
        return fetchList("tag?select=*&name=ilike.*" + name + "*");
    }
}