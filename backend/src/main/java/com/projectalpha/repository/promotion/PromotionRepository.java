package com.projectalpha.repository.promotion;

import com.projectalpha.model.promotion.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

    /**
     * İşletme ID'sine göre promosyonları bulur
     *
     * @param businessId İşletme ID'si
     * @return Promosyonlar listesi
     */
    @Query("SELECT p FROM Promotion p WHERE p.business.id = :businessId")
    List<Promotion> findByBusinessId(@Param("businessId") UUID businessId);

    /**
     * Aktif promosyonları listeler
     *
     * @return Aktif promosyonlar listesi
     */
    List<Promotion> findByIsActiveTrue();

    /**
     * Belirli bir tarihteki aktif promosyonları bulur
     *
     * @param date Tarih
     * @return Aktif promosyonlar listesi
     */
    @Query("SELECT p FROM Promotion p WHERE p.isActive = true AND " +
            "p.startDate <= :date AND (p.endDate IS NULL OR p.endDate >= :date)")
    List<Promotion> findActivePromotionsAtDate(@Param("date") OffsetDateTime date);

    /**
     * Süresi dolmuş ama hala aktif olan promosyonları bulur
     *
     * @param currentDate Güncel tarih
     * @return Süresi dolmuş promosyonlar
     */
    @Query("SELECT p FROM Promotion p WHERE p.isActive = true AND p.endDate < :currentDate")
    List<Promotion> findExpiredActivePromotions(@Param("currentDate") OffsetDateTime currentDate);
}