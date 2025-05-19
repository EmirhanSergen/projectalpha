package com.projectalpha.service.business;

import com.projectalpha.dto.business.BusinessDTO;
import com.projectalpha.repository.business.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;

    @Autowired
    public BusinessServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public List<BusinessDTO> getAllBusinesses() {
        return businessRepository.findAll();
    }

    @Override
    public Optional<BusinessDTO> getBusinessById(UUID id) {
        return businessRepository.findById(id);
    }

    @Override
    public List<BusinessDTO> searchBusinesses(String query) {
        return businessRepository.search(query);
    }
}