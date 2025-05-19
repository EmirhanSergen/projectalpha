package com.projectalpha.dto.business;

import java.util.UUID;

public class Business {
    private UUID id;
    private String name;
    private String description;
    private UUID addressId;
    private UUID ownerId;
    // Diğer gerekli alanlar

    // Boş constructor
    public Business() {
    }

    // Parametreli constructor
    public Business(UUID id, String name, String description, UUID addressId, UUID ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.addressId = addressId;
        this.ownerId = ownerId;
    }

    // Getter ve Setter metotları
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddress(String addressId) {this.addressId = UUID.fromString(addressId);}


    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }
}