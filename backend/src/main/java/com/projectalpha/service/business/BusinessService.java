package com.projectalpha.service.business;

import com.projectalpha.dto.business.BusinessDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessService {
    List<BusinessDTO> getAllBusinesses();
    Optional<BusinessDTO> getBusinessById(UUID id);
    List<BusinessDTO> searchBusinesses(String query);
}