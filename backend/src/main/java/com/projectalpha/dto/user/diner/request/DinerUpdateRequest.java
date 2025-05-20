package com.projectalpha.dto.user.diner.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Müşteri (Diner) profilini güncellemek için kullanılan DTO sınıfı.
 * Sadece güncellenebilir alanları içerir.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DinerUpdateRequest {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Map<String, Object> preferences;

    // Opsiyonel olarak eklenebilecek diğer alanlar:
    // Şifre değişikliği için eski ve yeni şifre alanları
    private String oldPassword;
    private String newPassword;

    // Avatar/Profil resmi URL'si
    private String profileImageUrl;

    // Adres bilgileri (gerekirse nested bir sınıf olarak da tanımlanabilir)
    private String address;
    private String city;
    private String postalCode;
    private String country;
}