package com.projectalpha.service.customlist;

import com.projectalpha.dto.list.CustomListDto;
import com.projectalpha.dto.business.BusinessDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomListService {
    CustomListDto createList(String userId, String listName);
    Optional<CustomListDto> getListById(UUID listId);
    List<CustomListDto> getListsByUser(String userId);
    void addBusinessToList(UUID listId, UUID businessId);
    void removeBusinessFromList(UUID listId, UUID businessId);
    List<BusinessDto> getBusinessesInList(UUID listId);
    void updateListName(UUID listId, String newName);
    void deleteList(UUID listId);
}