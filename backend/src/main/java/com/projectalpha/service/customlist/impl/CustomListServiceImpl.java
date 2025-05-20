package com.projectalpha.service.customlist.impl;

import com.projectalpha.dto.list.CustomListDto;
import com.projectalpha.dto.business.BusinessDto;
import com.projectalpha.repository.list.CustomListRepository;
import com.projectalpha.service.customlist.CustomListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomListServiceImpl implements CustomListService {

    private final CustomListRepository customListRepository;

    @Autowired
    public CustomListServiceImpl(CustomListRepository customListRepository) {
        this.customListRepository = customListRepository;
    }

    @Override
    public CustomListDto createList(String userId, String listName) {
        return customListRepository.create(userId, listName);
    }

    @Override
    public Optional<CustomListDto> getListById(UUID listId) {
        return customListRepository.findById(listId);
    }

    @Override
    public List<CustomListDto> getListsByUser(String userId) {
        return customListRepository.findByUserId(userId);
    }

    @Override
    public void addBusinessToList(UUID listId, UUID businessId) {
        customListRepository.addBusinessToList(listId, businessId);
    }

    @Override
    public void removeBusinessFromList(UUID listId, UUID businessId) {
        customListRepository.removeBusinessFromList(listId, businessId);
    }

    @Override
    public List<BusinessDto> getBusinessesInList(UUID listId) {
        // CustomListRepository'den gelen veriyi BusinessDto listesine dönüştürmeniz gerekiyor
        Optional<CustomListDto> customList = customListRepository.findById(listId);

        if (customList.isPresent()) {
            // CustomListDto içinde zaten businesses listesi vardır
            return customList.get().getBusinesses();
        }

        // Liste bulunamadıysa boş liste dön
        return new ArrayList<>();
    }

    @Override
    public void updateListName(UUID listId, String newName) {
        customListRepository.updateName(listId, newName);
    }

    @Override
    public void deleteList(UUID listId) {
        customListRepository.deleteList(listId);
    }
}