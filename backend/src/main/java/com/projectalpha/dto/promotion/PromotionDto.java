package com.projectalpha.dto.promotion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDto {
    private UUID id;
    private String title;
    private String description;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String conditions;
    private boolean active;
    private OffsetDateTime createdAt;
    private UUID businessId;
    private String businessName;
}