package com.projectalpha.dto.user.owner;

import lombok.Data;
import java.util.UUID;

@Data
public class OwnerDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}