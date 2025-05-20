package com.projectalpha.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.dto.user.diner.request.DinerUpdateRequest;
import com.projectalpha.dto.user.diner.response.DinerProfileResponse;
import com.projectalpha.model.user.UserProfileDiner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DinerMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Entity'i DTO'ya dönüştürür
     */
    public DinerProfileResponse toDto(UserProfileDiner entity) {
        if (entity == null) return null;

        DinerProfileResponse response = new DinerProfileResponse();
        // UUID'yi String'e dönüştür
        response.setId(entity.getId().toString());
        response.setUsername(entity.getUsername());
        response.setEmail(entity.getEmail());
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setPhoneNumber(entity.getPhoneNumber());

        // String olan preferences'ı Map<String, Object>'e dönüştür
        // Jackson ObjectMapper kullanarak
        if (entity.getPreferences() != null) {
            try {
                Map<String, Object> preferencesMap = objectMapper.readValue(
                        entity.getPreferences(),
                        new TypeReference<Map<String, Object>>() {}
                );
                response.setPreferences(preferencesMap);
            } catch (Exception e) {
                // Hata durumunda boş Map ya da null olarak ayarla
                response.setPreferences(null);
            }
        }

        // OffsetDateTime'dan LocalDateTime'a dönüştür
        response.setCreatedAt(entity.getCreatedAt() != null ?
                entity.getCreatedAt().toLocalDateTime() : null);
        response.setUpdatedAt(entity.getUpdatedAt() != null ?
                entity.getUpdatedAt().toLocalDateTime() : null);

        return response;
    }

    /**
     * Entity listesini DTO listesine dönüştürür
     */
    public List<DinerProfileResponse> toDtoList(List<UserProfileDiner> entityList) {
        if (entityList == null) return null;

        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * DTO'dan yeni bir entity oluşturur
     */
    public UserProfileDiner toEntity(DinerUpdateRequest dto) {
        if (dto == null) return null;

        UserProfileDiner entity = new UserProfileDiner();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    /**
     * Mevcut entity'i DTO'dan gelen verilerle günceller
     */
    public void updateEntityFromDto(DinerUpdateRequest dto, UserProfileDiner entity) {
        if (dto == null || entity == null) return;

        // Sadece null olmayan alanları güncelle
        if (dto.getFirstName() != null) {
            entity.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }
        if (dto.getPhoneNumber() != null) {
            entity.setPhoneNumber(dto.getPhoneNumber());
        }

        // Map<String, Object> olan preferences'ı String'e dönüştür
        if (dto.getPreferences() != null) {
            try {
                String preferencesJson = objectMapper.writeValueAsString(dto.getPreferences());
                entity.setPreferences(preferencesJson);
            } catch (Exception e) {
                // Hata durumunda işlem yapma veya loglama
            }
        }
    }
}