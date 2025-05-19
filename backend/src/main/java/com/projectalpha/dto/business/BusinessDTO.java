package com.projectalpha.dto.business;

import com.projectalpha.dto.business.address.AddressDTO;
import com.projectalpha.dto.user.owner.OwnerDTO;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class BusinessDTO {
    private UUID id;
    private String name;
    private String description;
    private String phoneNumber;
    private String email;
    private String website;
    private UUID addressId;
    private UUID ownerId;
    private OffsetDateTime createdAt;
    private boolean isActive;

    // İlişkili nesneler
    private AddressDTO address;
    private OwnerDTO owner;

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

}