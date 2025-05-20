package com.projectalpha.repository.business;

import com.projectalpha.model.business.Business;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessRepository {

    List<Business> findAll();

    Optional<Business> findById(UUID id);

    Optional<Business> findByName(String name);

    List<Business> findByNameContaining(String name);

    List<Business> findByLocation(String location);

    List<Business> findByCategory(String category);

    Business save(Business business);

    void deleteById(UUID id);


}