package com.projectalpha.mapper;

import com.projectalpha.dto.promotion.PromotionDto;
import com.projectalpha.dto.promotion.request.CreatePromotionRequest;
import com.projectalpha.dto.promotion.request.UpdatePromotionRequest;
import com.projectalpha.model.business.Business;
import com.projectalpha.model.promotion.Promotion;
import com.projectalpha.repository.business.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class PromotionMapper {

    private final BusinessRepository businessRepository;

    @Autowired
    public PromotionMapper(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    /**
     * Promotion entity'sini DTO'ya dönüştürür
     *
     * @param promotion Promotion entity
     * @return PromotionDto
     */
    public PromotionDto toDto(Promotion promotion) {
        if (promotion == null) {
            return null;
        }

        PromotionDto dto = new PromotionDto();
        dto.setId(promotion.getId());
        dto.setTitle(promotion.getTitle());
        dto.setDescription(promotion.getDescription());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setConditions(promotion.getConditions());
        dto.setActive(promotion.isActive());
        dto.setCreatedAt(promotion.getCreatedAt());

        if (promotion.getBusiness() != null) {
            dto.setBusinessId(promotion.getBusiness().getId());
            dto.setBusinessName(promotion.getBusiness().getName());
        }

        return dto;
    }

    /**
     * Create request'i entity'ye dönüştürür
     *
     * @param request CreatePromotionRequest
     * @return Promotion entity
     */
    public Promotion toEntity(CreatePromotionRequest request) {
        if (request == null) {
            return null;
        }

        Promotion promotion = new Promotion();
        promotion.setTitle(request.getTitle());
        promotion.setDescription(request.getDescription());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setConditions(request.getConditions());
        promotion.setActive(true);

        if (request.getBusinessId() != null) {
            Business business = businessRepository.findById(request.getBusinessId())
                    .orElseThrow(() -> new ResourceNotFoundException("İşletme bulunamadı: " + request.getBusinessId()));
            promotion.setBusiness(business);
        }

        return promotion;
    }

    /**
     * Update request bilgilerini entity'ye aktarır
     *
     * @param request UpdatePromotionRequest
     * @param promotion Güncellenecek entity
     */
    public void updateEntityFromDto(UpdatePromotionRequest request, Promotion promotion) {
        if (request == null) {
            return;
        }

        if (request.getTitle() != null) {
            promotion.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            promotion.setDescription(request.getDescription());
        }

        if (request.getStartDate() != null) {
            promotion.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            promotion.setEndDate(request.getEndDate());
        }

        if (request.getConditions() != null) {
            promotion.setConditions(request.getConditions());
        }

        if (request.getActive() != null) {
            promotion.setActive(request.getActive());
        }
    }
}