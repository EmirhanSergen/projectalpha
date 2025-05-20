package com.projectalpha.service.review;

import com.projectalpha.dto.review.ReviewDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewService {
    ReviewDto createReview(ReviewDto reviewDTO);
    Optional<ReviewDto> getReviewById(UUID reviewId);
    List<ReviewDto> getReviewsByBusiness(UUID businessId);
    List<ReviewDto> getReviewsByUser(String userId);
    void updateReview(UUID reviewId, ReviewDto reviewDTO);
    void deleteReview(UUID reviewId);
    double getAverageRatingForBusiness(UUID businessId);
}