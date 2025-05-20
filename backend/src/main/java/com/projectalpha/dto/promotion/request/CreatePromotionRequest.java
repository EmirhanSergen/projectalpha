package com.projectalpha.dto.promotion.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePromotionRequest {


    private String title;

    private String description;

    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    private String conditions;


    private UUID businessId;
}