package com.projectalpha.dto.business.address.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// Tamamen değişmesi lazım
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressRequest {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Double latitude;
    private Double longitude;
}