package com.projectalpha.repository.address.impl;

import com.projectalpha.repository.util.SupabaseHttpHelper;
import com.projectalpha.dto.business.address.AddressDTO;
import com.projectalpha.repository.address.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

    private final SupabaseHttpHelper httpHelper;

    @Autowired
    public AddressRepositoryImpl(SupabaseHttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    private List<AddressDTO> fetchList(String path) {
        return httpHelper.fetchList(path, AddressDTO.class);
    }

    private AddressDTO fetchSingle(String path) {
        return httpHelper.fetchSingle(path, AddressDTO.class);
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