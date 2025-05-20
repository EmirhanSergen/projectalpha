package com.projectalpha.dto.business.address.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


// değişecek
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressRequest {
    private UUID id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Double latitude;
    private Double longitude;
}