package com.projectalpha.repository.list;

import com.projectalpha.dto.list.CustomListDto;
import com.projectalpha.model.business.Business;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomListRepository {
    CustomListDto create(String userId, String listName);
    Optional<CustomListDto> findById(UUID listId);
    List<CustomListDto> findByUserId(String userId);
    void addBusinessToList(UUID listId, UUID businessId);
    void removeBusinessFromList(UUID listId, UUID businessId);
    List<CustomListDto> getBusinessesInList(UUID listId);
    void updateName(UUID listId, String newName);
    void deleteList(UUID listId);
}