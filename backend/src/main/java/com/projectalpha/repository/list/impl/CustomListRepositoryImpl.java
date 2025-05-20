package com.projectalpha.repository.list.impl;

import com.projectalpha.dto.business.BusinessDto;
import com.projectalpha.dto.list.CustomListDto;
import com.projectalpha.exception.ResourceNotFoundException;
import com.projectalpha.mapper.BusinessMapper;
import com.projectalpha.mapper.CustomListMapper;
import com.projectalpha.model.business.Business;
import com.projectalpha.model.list.CustomList;
import com.projectalpha.model.list.CustomListItem;
import com.projectalpha.repository.business.BusinessRepository;
import com.projectalpha.repository.list.CustomListItemRepository;
import com.projectalpha.repository.list.CustomListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CustomListRepositoryImpl implements CustomListRepository {

    private final JpaCustomListRepository jpaCustomListRepository;
    private final CustomListItemRepository customListItemRepository;
    private final BusinessRepository businessRepository;
    private final CustomListMapper customListMapper;
    private final BusinessMapper businessMapper;

    @Autowired
    public CustomListRepositoryImpl(
            JpaCustomListRepository jpaCustomListRepository,
            CustomListItemRepository customListItemRepository,
            BusinessRepository businessRepository,
            CustomListMapper customListMapper,
            BusinessMapper businessMapper) {
        this.jpaCustomListRepository = jpaCustomListRepository;
        this.customListItemRepository = customListItemRepository;
        this.businessRepository = businessRepository;
        this.customListMapper = customListMapper;
        this.businessMapper = businessMapper;
    }

    @Override
    @Transactional
    public CustomListDto create(String userId, String listName) {
        CustomList customList = new CustomList();
        customList.setUserId(UUID.fromString(userId));
        customList.setName(listName);
        customList.setLikeCounter(0);
        customList.setIsPublic(false);

        CustomList savedList = jpaCustomListRepository.save(customList);
        return customListMapper.toDto(savedList);
    }

    @Override
    public Optional<CustomListDto> findById(UUID listId) {
        return jpaCustomListRepository.findById(listId)
                .map(list -> {
                    CustomListDto dto = customListMapper.toDto(list);
                    loadBusinessesForList(dto);
                    return dto;
                });
    }

    @Override
    public List<CustomListDto> findByUserId(String userId) {
        List<CustomList> userLists = jpaCustomListRepository.findByUserId(UUID.fromString(userId));
        return userLists.stream()
                .map(list -> {
                    CustomListDto dto = customListMapper.toDto(list);
                    loadBusinessesForList(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private void loadBusinessesForList(CustomListDto listDto) {
        List<UUID> businessIds = customListItemRepository.findBusinessIdsByListId(listDto.getId());
        if (!businessIds.isEmpty()) {
            List<Business> businesses = businessRepository.findAllById(businessIds);
            listDto.setBusinesses(businesses.stream()
                    .map(businessMapper::toDto)
                    .collect(Collectors.toList()));
        }
    }

    @Override
    @Transactional
    public void addBusinessToList(UUID listId, UUID businessId) {
        CustomList customList = jpaCustomListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("Liste bulunamadı: " + listId));

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("İşletme bulunamadı: " + businessId));

        // İşletme zaten listede var mı kontrol et
        boolean alreadyExists = customListItemRepository.existsByCustomListIdAndBusinessId(listId, businessId);

        if (!alreadyExists) {
            CustomListItem item = new CustomListItem();
            item.setCustomList(customList);
            item.setBusinessId(businessId);

            customListItemRepository.save(item);
        }
    }

    @Override
    @Transactional
    public void removeBusinessFromList(UUID listId, UUID businessId) {
        customListItemRepository.deleteByCustomListIdAndBusinessId(listId, businessId);
    }

    @Override
    public List<CustomListDto> getBusinessesInList(UUID listId) {
        CustomList customList = jpaCustomListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("Liste bulunamadı: " + listId));

        CustomListDto listDto = customListMapper.toDto(customList);
        loadBusinessesForList(listDto);

        return List.of(listDto);
    }

    @Override
    @Transactional
    public void updateName(UUID listId, String newName) {
        CustomList customList = jpaCustomListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("Liste bulunamadı: " + listId));

        customList.setName(newName);
        jpaCustomListRepository.save(customList);
    }

    @Override
    @Transactional
    public void deleteList(UUID listId) {
        // Önce liste-işletme ilişkilerini sil
        customListItemRepository.deleteByCustomListId(listId);

        // Sonra listeyi sil
        jpaCustomListRepository.deleteById(listId);
    }

    // JPA Repository interface
    public interface JpaCustomListRepository extends JpaRepository<CustomList, UUID> {
        List<CustomList> findByUserId(UUID userId);
    }
}