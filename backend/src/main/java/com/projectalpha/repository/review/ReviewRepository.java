package com.projectalpha.repository.review;

import com.projectalpha.dto.review.ReviewDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository {

    /**
     * Yeni bir yorum kaydeder
     *
     * @param reviewDTO Kaydedilecek yorum
     * @return Kaydedilen yorum
     */
    ReviewDto save(ReviewDto reviewDTO);

    /**
     * ID'ye göre yorum arar
     *
     * @param reviewId Aranacak yorumun ID'si
     * @return Bulunan yorum veya boş Optional
     */
    Optional<ReviewDto> findById(UUID reviewId);

    /**
     * İşletme ID'sine göre yorumları listeler
     *
     * @param businessId İşletme ID'si
     * @return İşletmeye ait yorumlar listesi
     */
    List<ReviewDto> findByBusinessId(UUID businessId);

    /**
     * Kullanıcı ID'sine göre yorumları listeler
     *
     * @param userId Kullanıcı ID'si
     * @return Kullanıcının yaptığı yorumlar listesi
     */
    List<ReviewDto> findByUserId(String userId);

    /**
     * Yorum günceller
     *
     * @param reviewId Güncellenecek yorumun ID'si
     * @param reviewDTO Güncel yorum bilgileri
     */
    void update(UUID reviewId, ReviewDto reviewDTO);

    /**
     * Yorumu siler
     *
     * @param reviewId Silinecek yorumun ID'si
     */
    void delete(UUID reviewId);

    /**
     * İşletme için ortalama puanı hesaplar
     *
     * @param businessId İşletme ID'si
     * @return Ortalama puan
     */
    double calculateAverageRating(UUID businessId);
}