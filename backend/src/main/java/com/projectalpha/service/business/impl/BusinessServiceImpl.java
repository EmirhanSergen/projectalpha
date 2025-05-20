package com.projectalpha.service.business.impl;

import com.projectalpha.dto.business.BusinessDto;
import com.projectalpha.mapper.BusinessMapper;
import com.projectalpha.model.business.Business;
import com.projectalpha.repository.business.BusinessRepository;
import com.projectalpha.service.business.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BusinessServiceImpl implements BusinessService {
    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;

    @Autowired
    public BusinessServiceImpl(BusinessRepository businessRepository, BusinessMapper businessMapper) {
        this.businessRepository = businessRepository;
        this.businessMapper = businessMapper;
    }

    @Override
    public List<BusinessDto> getAllBusinesses() {
        // Repository'den entity'leri al ve mapper ile DTO'lara dönüştür
        List<Business> businesses = businessRepository.findAll();
        return businessMapper.toDtoList(businesses);
    }

    @Override
    public Optional<BusinessDto> getBusinessById(UUID id) {
        // Repository'den entity'yi al ve mapper ile DTO'ya dönüştür
        return businessRepository.findById(id)
                .map(businessMapper::toDto);
    }

    @Override
    public List<BusinessDto> searchBusinesses(String query) {
        // Basit bir arama işlemi - isim içerecek şekilde arama yap
        List<Business> businesses = businessRepository.findByNameContaining(query);
        return businessMapper.toDtoList(businesses);
    }
}