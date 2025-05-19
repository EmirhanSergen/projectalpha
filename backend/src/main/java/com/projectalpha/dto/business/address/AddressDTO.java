package com.projectalpha.dto.business.address;

import lombok.Data;
import java.util.UUID;

@Data
public class AddressDTO {
    private UUID id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Double latitude;
    private Double longitude;
}