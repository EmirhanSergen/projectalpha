package com.projectalpha.dto.user.diner.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DinerProfileResponse {

    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Map<String, Object> preferences;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}