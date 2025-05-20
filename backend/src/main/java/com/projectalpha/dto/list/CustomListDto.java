package com.projectalpha.dto.list;

import com.projectalpha.dto.business.BusinessDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomListDto {
    private UUID id;
    private String name;
    private UUID userId;
    private UUID userProfileDinerId;
    private Integer likeCounter;
    private Boolean isPublic;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<BusinessDto> businesses = new ArrayList<>();
}