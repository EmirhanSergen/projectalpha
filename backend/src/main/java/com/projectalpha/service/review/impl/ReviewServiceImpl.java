package com.projectalpha.service.review.impl;

import com.projectalpha.dto.review.ReviewDto;
import com.projectalpha.repository.review.ReviewRepository;
import com.projectalpha.service.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDTO) {
        // İş mantığı kontrolleri eklenebilir
        return reviewRepository.save(reviewDTO);
    }

    @Override
    public Optional<ReviewDto> getReviewById(UUID reviewId) {
        return reviewRepository.findById(reviewId);
    }

    @Override
    public List<ReviewDto> getReviewsByBusiness(UUID businessId) {
        return reviewRepository.findByBusinessId(businessId);
    }

    @Override
    public List<ReviewDto> getReviewsByUser(String userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Override
    public void updateReview(UUID reviewId, ReviewDto reviewDTO) {
        // İş mantığı kontrolleri eklenebilir
        reviewRepository.update(reviewId, reviewDTO);
    }

    @Override
    public void deleteReview(UUID reviewId) {
        reviewRepository.delete(reviewId);
    }

    @Override
    public double getAverageRatingForBusiness(UUID businessId) {
        return reviewRepository.calculateAverageRating(businessId);
    }
}