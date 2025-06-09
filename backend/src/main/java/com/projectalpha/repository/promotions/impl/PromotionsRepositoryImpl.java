package com.projectalpha.repository.promotions.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.config.thirdparty.SupabaseConfig;
import com.projectalpha.dto.business.address.AddressDTO;
import com.projectalpha.dto.promotions.PromotionInfoForGeneral;
import com.projectalpha.dto.promotions.PromotionsSupabase;
import com.projectalpha.dto.promotions.newPromotionRequest;
import com.projectalpha.repository.promotions.PromotionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.projectalpha.util.SupabaseHttpHelper;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PromotionsRepositoryImpl implements PromotionsRepository {

    private final SupabaseConfig supabaseConfig;
    private final SupabaseHttpHelper httpHelper;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(PromotionsRepositoryImpl.class);

    @Autowired
    public PromotionsRepositoryImpl(SupabaseConfig supabaseConfig, SupabaseHttpHelper httpHelper) {
        this.supabaseConfig = supabaseConfig;
        this.httpHelper = httpHelper;
    }

    public PromotionsSupabase newPromotion(String businessId, newPromotionRequest request){
        try {
            Map<String, Object> promotionPayload = Map.of(
                    "title", request.getTitle(),
                    "description", request.getDescription(),
                    "startat", request.getStartDate(),
                    "endat", request.getEndDate(),
                    "amount", request.getAmount(),
                    "active", request.getIsActive(),
                    "business_id", businessId
            );
            String promotionJson = mapper.writeValueAsString(promotionPayload);
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/promotion";
            String resp = httpHelper.post(url, promotionJson, "return=representation");
            PromotionsSupabase[] promotions = mapper.readValue(resp, PromotionsSupabase[].class);
            PromotionsSupabase promotion = promotions[0] != null ? promotions[0] : promotions[1] != null ? promotions[1] : null;

            logger.info("Promosyon başarıyla oluşturuldu: {}", resp);

            return promotion;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Promosyon oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    public void deletePromotion(String businessId, String promotionId){
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/promotion?id=eq." + promotionId + "&business_id=eq." + businessId;
            httpHelper.delete(url, "return=minimal");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Promosyon silinirken hata oluştu: " + e.getMessage());
        }
    }
    public void updatePromotion(String businessId, String promotionId, newPromotionRequest request){
        try {
            Map<String, Object> promotionPayload = Map.of(
                    "title", request.getTitle(),
                    "description", request.getDescription(),
                    "startat", request.getStartDate(),
                    "endat", request.getEndDate(),
                    "amount", request.getAmount(),
                    "active", request.getIsActive()
            );
            String promotionJson = mapper.writeValueAsString(promotionPayload);
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/promotion?id=eq." + promotionId + "&business_id=eq." + businessId;
            String resp = httpHelper.patch(url, promotionJson, "return=representation");

            logger.info("Promosyon başarıyla güncellendi: {}", resp);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Promosyon güncellenirken hata oluştu: " + e.getMessage());
        }
    }
    public PromotionInfoForGeneral getPromotionInfo(String businessId, String promotionId){
        return null;
    }

    @Override
    public List<PromotionsSupabase> getPromotions(String businessId) {
        try {
            String url = supabaseConfig.getSupabaseUrl()
                    + "/rest/v1/promotion?business_id=eq." + businessId;
            String body = httpHelper.get(url);
            JsonNode root = mapper.readTree(body);
            List<PromotionsSupabase> listOfPromotions = new ArrayList<>();

            if (root.isArray()) {
                for (JsonNode node : root) {
                    PromotionsSupabase promotion = mapper.treeToValue(node, PromotionsSupabase.class);
                    listOfPromotions.add(promotion);
                }
            }

            return listOfPromotions;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Promosyonlar alınırken hata oluştu: " + e.getMessage());
        }
    }
    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredPromotions() {
        try {
            String today = OffsetDateTime.now(ZoneOffset.ofHours(3)).toString(); // +03 saat diliminde şimdi
            String url = supabaseConfig.getSupabaseUrl()
                    + "/rest/v1/promotion?endat=lt." + today;

            String getResp = httpHelper.get(url);
            JsonNode promos = mapper.readTree(getResp);

            if (!promos.isArray() || promos.size() == 0) {
                logger.info("Silinecek promosyon yok.");
                return;
            }

            // Her promosyonu tek tek sil
            for (JsonNode promo : promos) {
                String id = promo.get("id").asText();

                String deleteUrl = supabaseConfig.getSupabaseUrl() + "/rest/v1/promotion?id=eq." + id;

                HttpRequest deleteRequest = HttpRequest.newBuilder()
                        .uri(URI.create(deleteUrl))
                        .header("apikey", supabaseConfig.getSupabaseApiKey())
                        .header("Authorization", "Bearer " + supabaseConfig.getSupabaseSecretKey())
                        .header("Content-Type", "application/json")
                        .header("Prefer", "return=representation")
                        .method("DELETE", HttpRequest.BodyPublishers.noBody())
                        .build();

                HttpResponse<String> deleteResponse = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

                if (deleteResponse.statusCode() == 204 || deleteResponse.statusCode() == 200) {
                    logger.info("Promosyon silindi: {}", id);
                } else {
                    logger.error("Promosyon silinemedi: {} - {}", id, deleteResponse.body());
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Süresi dolan promosyonlar silinirken hata oluştu: " + e.getMessage());
        }
    }
}
