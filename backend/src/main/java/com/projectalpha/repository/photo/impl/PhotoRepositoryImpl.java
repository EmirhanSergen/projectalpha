package com.projectalpha.repository.photo.impl;

import com.projectalpha.repository.util.SupabaseHttpHelper;
import com.projectalpha.dto.business.photo.PhotoDTO;
import com.projectalpha.repository.photo.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhotoRepositoryImpl implements PhotoRepository {
    private final SupabaseHttpHelper httpHelper;

    @Autowired
    public PhotoRepositoryImpl(SupabaseHttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    private List<PhotoDTO> fetchList(String path) {
        return httpHelper.fetchList(path, PhotoDTO.class);
    }

    private PhotoDTO fetchSingle(String path) {
        return httpHelper.fetchSingle(path, PhotoDTO.class);
    }

    @Override
    public List<PhotoDTO> findAll() {
        return fetchList("photo?select=*");
    }

    @Override
    public PhotoDTO findById(String id) {
        return fetchSingle("photo?select=*&id=eq." + id);
    }

    @Override
    public List<PhotoDTO> findByBusinessId(String businessId) {
        return fetchList("photo?select=*&business_id=eq." + businessId);
    }
}