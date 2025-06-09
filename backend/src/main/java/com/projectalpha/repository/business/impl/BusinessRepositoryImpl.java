package com.projectalpha.repository.business.impl;

import com.projectalpha.repository.util.SupabaseHttpHelper;
import com.projectalpha.dto.business.BusinessDTO;
import com.projectalpha.repository.business.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository implementation using Supabase REST API via HTTP calls.
 * Mirrors the pattern of AuthRepositoryImpl.
 */
@Repository
public class BusinessRepositoryImpl implements BusinessRepository {

    private final SupabaseHttpHelper httpHelper;

    @Autowired
    public BusinessRepositoryImpl(SupabaseHttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    private List<BusinessDTO> fetchList(String path) {
        return httpHelper.fetchList(path, BusinessDTO.class);
    }

    private BusinessDTO fetchSingle(String path) {
        return httpHelper.fetchSingle(path, BusinessDTO.class);
    }

    @Override
    public List<BusinessDTO> findAll() {
        // select all fields
        return fetchList("business?select=*");
    }

    @Override
    public BusinessDTO findById(String id) {
        // filter by id
        return fetchSingle("business?select=*&id=eq." + id);
    }

    @Override
    public List<BusinessDTO> findByNameContainingIgnoreCase(String name) {
        // case-insensitive 'ilike'
        return fetchList("business?select=*&name=ilike.*" + name + "*");
    }

    @Override
    public List<BusinessDTO> findByOwnerId(Long ownerId) {
        return fetchList("business?select=*&owner_id1=eq." + ownerId);
    }

    @Override
    public List<BusinessDTO> findTop5ByOrderByAvgRatingDesc() {
        return fetchList("business?select=*&order=avg_rating.desc&limit=5");
    }

    @Override
    public List<BusinessDTO> findWithActivePromotions() {
        return fetchList("business?select=*,promotions(*)&promotions.start_time=lte.now&promotions.end_time=gte.now");
    }

    @Override
    public List<BusinessDTO> findByTag(UUID tagId) {
        return fetchList("business?select=*&tags=eq." + tagId);
    }
}
