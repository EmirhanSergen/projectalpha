package com.projectalpha.repository.reviews.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.config.thirdparty.SupabaseConfig;
import com.projectalpha.dto.review.ReviewSupabase;
import com.projectalpha.dto.review.newReviewRequest;
import com.projectalpha.repository.reviews.ReviewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import com.projectalpha.util.SupabaseHttpHelper;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ReviewsRepositoryImpl implements ReviewsRepository {

    private final SupabaseConfig supabaseConfig;
    private final SupabaseHttpHelper httpHelper;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(ReviewsRepositoryImpl.class);

    @Autowired
    public ReviewsRepositoryImpl(SupabaseConfig supabaseConfig, SupabaseHttpHelper httpHelper) {
        this.supabaseConfig = supabaseConfig;
        this.httpHelper = httpHelper;
    }

    @Override
    public List<ReviewSupabase> getReviewsByUserId(String userId) {
        try {
            String url = supabaseConfig.getSupabaseUrl()
                    + "/rest/v1/review"
                    + "?select="
                    // review alanları
                    + "id,comment,rating,created_at,review_photo_url,user_id,business_id,"
                    // business ilişkisinin gömülmesi; tablo adı “photo”
                    + "business:business_id("
                    + "name,"
                    + "avg_rating,"
                    + "description,"
                    + "photo(id,url,caption,isCover)"
                    + ")"
                    // filtre ve sıralama
                    + "&user_id=eq." + userId
                    + "&order=created_at.desc";

            String body = httpHelper.get(url);

            JsonNode root = mapper.readTree(body);
            List<ReviewSupabase> reviews = new ArrayList<>();
            if (root.isArray()) {
                for (JsonNode node : root) {
                    reviews.add(mapper.treeToValue(node, ReviewSupabase.class));
                }
            }
            return reviews;
        } catch (Exception e) {
            throw new RuntimeException("Değerlendirmeler alınırken hata oluştu: " + e.getMessage(), e);
        }
    }

    public void saveReview(String dinerId, String businessId, newReviewRequest request) {
        try {
            Map<String, Object> reviewPayload = Map.of(
                    "comment", request.getComment(),
                    "rating", request.getRating(),
                    "created_at", OffsetDateTime.now().toString(),
                    "business_id", businessId,
                    "user_id", dinerId
            );

            String reviewsJson = mapper.writeValueAsString(reviewPayload);
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/review";
            String resp = httpHelper.post(url, reviewsJson, "return=representation");


            logger.info("Review başarıyla oluşturuldu: {}", response.body());


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Değerlendirme oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    public void deleteReview(String reviewId){
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/review/" + reviewId;
            httpHelper.delete(url, "return=minimal");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Değerlendirme silinirken hata oluştu: " + e.getMessage());
        }
    }

    public List<ReviewSupabase> getReviewsByBusinessId(String businessId) {
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/review?business_id=eq." + businessId;
            String body = httpHelper.get(url);
            JsonNode root = mapper.readTree(body);
            List<ReviewSupabase> listOfReviews = new ArrayList<>();

            if (root.isArray()) {
                for (JsonNode node : root) {
                    ReviewSupabase review = mapper.treeToValue(node, ReviewSupabase.class);
                    listOfReviews.add(review);
                }
            }

            return listOfReviews;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Değerlendirmeler alınırken hata oluştu: " + e.getMessage());
        }
    }

    public void updateRating(String businessId, double rating){
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/business?id=eq." + businessId;
            String resp = httpHelper.patch(url, "{\"avg_rating\": " + rating + "}", "return=representation");


            logger.info("Review başarıyla oluşturuldu: {}", response.body());


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Değerlendirme oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    public void setViewed(String reviewId){
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/review?id=eq." + reviewId;
            String resp = httpHelper.patch(url, "{\"isViewed\": true}", "return=representation");

            logger.info("Review görüntülendi: {}", response.body());


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Değerlendirme oluşturulurken hata oluştu: " + e.getMessage());
        }
    }


}
