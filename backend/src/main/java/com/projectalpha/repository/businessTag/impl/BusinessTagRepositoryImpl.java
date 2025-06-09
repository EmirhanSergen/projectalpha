package com.projectalpha.repository.businessTag.impl;

import com.projectalpha.repository.util.SupabaseHttpHelper;
import com.projectalpha.dto.business.businessTag.BusinessTagDTO;
import com.projectalpha.repository.businessTag.BusinessTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BusinessTagRepositoryImpl implements BusinessTagRepository {
    private final SupabaseHttpHelper httpHelper;

    @Autowired
    public BusinessTagRepositoryImpl(SupabaseHttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    private List<BusinessTagDTO> fetchList(String path) {
        return httpHelper.fetchList(path, BusinessTagDTO.class);
    }

    private BusinessTagDTO fetchSingle(String path) {
        return httpHelper.fetchSingle(path, BusinessTagDTO.class);
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