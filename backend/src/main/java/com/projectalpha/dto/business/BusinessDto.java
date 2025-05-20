package com.projectalpha.dto.business;

import com.projectalpha.dto.business.address.AddressDto;
import com.projectalpha.dto.business.photo.PhotoDto;
import com.projectalpha.dto.business.settings.RestaurantSettingsDto;
import com.projectalpha.dto.business.tag.BusinessTagDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDto {
    private UUID id;
    private String name;
    private String description;
    private String category;
    private AddressDto address;
    private String phoneNumber;
    private String email;
    private String website;
    private List<OperatingHourDto> operatingHours;
    private List<PhotoDto> photos;
    private RestaurantSettingsDto settings;
    private List<BusinessTagDto> tags;
    private Double averageRating;
    private Integer reviewCount;
}