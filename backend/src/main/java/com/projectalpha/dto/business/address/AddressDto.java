package com.projectalpha.dto.business.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// Bunun değişmesi lazım tekrardan
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private UUID id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Double latitude;
    private Double longitude;
}