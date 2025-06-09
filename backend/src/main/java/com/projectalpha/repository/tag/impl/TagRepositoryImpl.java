package com.projectalpha.repository.tag.impl;

import com.projectalpha.repository.util.SupabaseHttpHelper;
import com.projectalpha.dto.business.tag.TagDTO;
import com.projectalpha.repository.tag.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private final SupabaseHttpHelper httpHelper;

    @Autowired
    public TagRepositoryImpl(SupabaseHttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    private List<TagDTO> fetchList(String path) {
        return httpHelper.fetchList(path, TagDTO.class);
    }

    private TagDTO fetchSingle(String path) {
        return httpHelper.fetchSingle(path, TagDTO.class);
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