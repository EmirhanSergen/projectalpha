package com.projectalpha.service.promotion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.projectalpha.dto.promotion.PromotionDto;
import com.projectalpha.dto.promotion.request.CreatePromotionRequest;
import com.projectalpha.dto.promotion.request.UpdatePromotionRequest;

/**
 * Promosyon işlemlerini yöneten servis interface'i
 */
public interface PromotionService {

    /**
     * Yeni bir promosyon oluşturur
     *
     * @param createRequest Promosyon oluşturma bilgileri
     * @return Oluşturulan promosyon detayları
     */
    PromotionDto createPromotion(CreatePromotionRequest createRequest);

    /**
     * ID'ye göre promosyon arar
     *
     * @param id Promosyon ID'si
     * @return Bulunan promosyon veya boş Optional
     */
    Optional<PromotionDto> getPromotionById(UUID id);

    /**
     * İşletmeye ait tüm promosyonları listeler
     *
     * @param businessId İşletme ID'si
     * @return Promosyonlar listesi
     */
    List<PromotionDto> getPromotionsByBusiness(UUID businessId);

    /**
     * Aktif promosyonları listeler
     *
     * @return Aktif promosyonlar listesi
     */
    List<PromotionDto> getActivePromotions();

    /**
     * Promosyonu günceller
     *
     * @param id Güncellenecek promosyon ID'si
     * @param updateRequest Güncelleme bilgileri
     * @return Güncellenmiş promosyon
     */
    PromotionDto updatePromotion(UUID id, UpdatePromotionRequest updateRequest);

    /**
     * Promosyonu siler
     *
     * @param id Silinecek promosyon ID'si
     */
    void deletePromotion(UUID id);

    /**
     * Promosyonu aktifleştirir
     *
     * @param id Aktifleştirilecek promosyon ID'si
     * @return Güncellenmiş promosyon
     */
    PromotionDto activatePromotion(UUID id);

    /**
     * Promosyonu devre dışı bırakır
     *
     * @param id Devre dışı bırakılacak promosyon ID'si
     * @return Güncellenmiş promosyon
     */
    PromotionDto deactivatePromotion(UUID id);
}