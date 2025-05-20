package com.projectalpha.dto.user.owner.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerLoginResponse {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String companyName;
    private String taxId;
    private List<UUID> businessIds;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}