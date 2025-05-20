package com.projectalpha.service.business;

import com.projectalpha.dto.business.BusinessDto;
import com.projectalpha.dto.business.BusinessDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessService {
    List<BusinessDto> getAllBusinesses();
    Optional<BusinessDto> getBusinessById(UUID id);
    List<BusinessDto> searchBusinesses(String query);
}
