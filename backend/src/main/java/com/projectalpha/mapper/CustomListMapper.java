package com.projectalpha.mapper;

import com.projectalpha.dto.list.CustomListDto;
import com.projectalpha.model.list.CustomList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Özel liste varlıklarını DTO'lara ve DTO'ları özel liste varlıklarına dönüştüren mapper sınıfı
 */
@Component
public class CustomListMapper {

    /**
     * Özel liste varlığını DTO'ya dönüştürür
     *
     * @param customList Özel liste varlığı
     * @return Özel liste DTO
     */
    public CustomListDto toDto(CustomList customList) {
        if (customList == null) {
            return null;
        }

        CustomListDto dto = new CustomListDto();
        dto.setId(customList.getId());
        dto.setName(customList.getName());
        dto.setUserId(customList.getUserId());
        dto.setUserProfileDinerId(customList.getUserProfileDinerId());
        dto.setLikeCounter(customList.getLikeCounter());
        dto.setIsPublic(customList.getIsPublic());
        dto.setCreatedAt(customList.getCreatedAt());
        dto.setUpdatedAt(customList.getUpdatedAt());
        dto.setBusinesses(new ArrayList<>()); // İş yerleri ayrı bir işlemle doldurulacak

        return dto;
    }

    /**
     * Özel liste DTO'sunu varlığa dönüştürür
     *
     * @param customListDto Özel liste DTO
     * @return Özel liste varlığı
     */
    public CustomList toEntity(CustomListDto customListDto) {
        if (customListDto == null) {
            return null;
        }

        CustomList customList = new CustomList();
        customList.setId(customListDto.getId());
        customList.setName(customListDto.getName());
        customList.setUserId(customListDto.getUserId());
        customList.setUserProfileDinerId(customListDto.getUserProfileDinerId());
        customList.setLikeCounter(customListDto.getLikeCounter());
        customList.setIsPublic(customListDto.getIsPublic());
        customList.setCreatedAt(customListDto.getCreatedAt());
        customList.setUpdatedAt(customListDto.getUpdatedAt());

        // İlişkilerin (CustomListItem) eklenmesi bu mapper'ın kapsamı dışındadır

        return customList;
    }

    /**
     * Özel liste varlığını günceller
     *
     * @param existingList Mevcut liste varlığı
     * @param updatedDto Güncellenmiş DTO verisi
     */
    public void updateEntityFromDto(CustomList existingList, CustomListDto updatedDto) {
        if (existingList == null || updatedDto == null) {
            return;
        }

        // Sadece güncellenebilir alanları güncelle
        if (updatedDto.getName() != null) {
            existingList.setName(updatedDto.getName());
        }

        if (updatedDto.getLikeCounter() != null) {
            existingList.setLikeCounter(updatedDto.getLikeCounter());
        }

        if (updatedDto.getIsPublic() != null) {
            existingList.setIsPublic(updatedDto.getIsPublic());
        }

        // İlişkiler (CustomListItem) bu mapper'ın kapsamı dışındadır
    }

    /**
     * Özel liste varlıklarının listesini DTO listesine dönüştürür
     *
     * @param customLists Özel liste varlıkları listesi
     * @return Özel liste DTO'ları listesi
     */
    public List<CustomListDto> toDtoList(List<CustomList> customLists) {
        if (customLists == null) {
            return null;
        }

        return customLists.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Özel liste DTO'larının listesini varlık listesine dönüştürür
     *
     * @param customListDtos Özel liste DTO'ları listesi
     * @return Özel liste varlıkları listesi
     */
    public List<CustomList> toEntityList(List<CustomListDto> customListDtos) {
        if (customListDtos == null) {
            return null;
        }

        return customListDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}