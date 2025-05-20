package com.projectalpha.service.promotion.impl;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectalpha.dto.promotion.PromotionDto;
import com.projectalpha.dto.promotion.request.CreatePromotionRequest;
import com.projectalpha.dto.promotion.request.UpdatePromotionRequest;
import com.projectalpha.exception.ResourceNotFoundException;
import com.projectalpha.mapper.PromotionMapper;
import com.projectalpha.model.promotion.Promotion;
import com.projectalpha.repository.promotion.PromotionRepository;
import com.projectalpha.service.promotion.PromotionService;

/**
 * Promosyon servis implementasyonu
 */
@Service
public class PromotionRepositoryImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Autowired
    public PromotionRepositoryImpl(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    @Override
    @Transactional
    public PromotionDto createPromotion(CreatePromotionRequest createRequest) {
        Promotion promotion = promotionMapper.toEntity(createRequest);
        promotion.setActive(true);
        // onCreate() metodu @PrePersist ile otomatik olarak çağrılacak ve createdAt alanını dolduracak

        Promotion savedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(savedPromotion);
    }

    @Override
    public Optional<PromotionDto> getPromotionById(UUID id) {
        return promotionRepository.findById(id)
                .map(promotionMapper::toDto);
    }

    @Override
    public List<PromotionDto> getPromotionsByBusiness(UUID businessId) {
        return promotionRepository.findByBusinessId(businessId)
                .stream()
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PromotionDto> getActivePromotions() {
        return promotionRepository.findByIsActiveTrue()
                .stream()
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Belirli bir tarihteki aktif promosyonları getirir
     *
     * @param date Tarih
     * @return Aktif promosyonlar listesi
     */
    public List<PromotionDto> getActivePromotionsAtDate(OffsetDateTime date) {
        return promotionRepository.findActivePromotionsAtDate(date)
                .stream()
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Süresi dolmuş promosyonları devre dışı bırakır
     */
    @Transactional
    public void deactivateExpiredPromotions() {
        OffsetDateTime currentDate = OffsetDateTime.now();
        List<Promotion> expiredPromotions = promotionRepository.findExpiredActivePromotions(currentDate);

        for (Promotion promotion : expiredPromotions) {
            promotion.setActive(false);
        }

        if (!expiredPromotions.isEmpty()) {
            promotionRepository.saveAll(expiredPromotions);
        }
    }

    @Override
    @Transactional
    public PromotionDto updatePromotion(UUID id, UpdatePromotionRequest updateRequest) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promosyon bulunamadı: " + id));

        promotionMapper.updateEntityFromDto(updateRequest, promotion);

        Promotion updatedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(updatedPromotion);
    }

    @Override
    @Transactional
    public void deletePromotion(UUID id) {
        if (!promotionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Promosyon bulunamadı: " + id);
        }
        promotionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PromotionDto activatePromotion(UUID id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promosyon bulunamadı: " + id));

        promotion.setActive(true);

        Promotion updatedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(updatedPromotion);
    }

    @Override
    @Transactional
    public PromotionDto deactivatePromotion(UUID id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promosyon bulunamadı: " + id));

        promotion.setActive(false);

        Promotion updatedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(updatedPromotion);
    }
}