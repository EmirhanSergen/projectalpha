package com.projectalpha.dto.business.request;

import com.projectalpha.dto.business.OperatingHourDto;
import com.projectalpha.dto.business.address.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBusinessRequest {
    private String name;
    private String description;
    private String category;
    private AddressDto address;
    private String phoneNumber;
    private String email;
    private String website;
    private List<OperatingHourDto> operatingHours;
    // Diğer gerekli alanlar
}