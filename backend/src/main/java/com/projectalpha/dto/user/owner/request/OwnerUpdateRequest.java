package com.projectalpha.dto.user.owner.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * İşletme sahibi profil güncelleme isteği için DTO sınıfı
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerUpdateRequest {

    /**
     * İşletme sahibinin adı
     */
    private String firstName;

    /**
     * İşletme sahibinin soyadı
     */
    private String lastName;

    /**
     * İşletme sahibinin e-posta adresi
     */
    private String email;

    /**
     * İşletme sahibinin telefon numarası
     */
    private String phoneNumber;

    /**
     * İşletme sahibinin profil fotoğrafı URL'si
     */
    private String profilePhotoUrl;

    /**
     * İşletme sahibinin kısa biyografisi
     */
    private String biography;

    /**
     * İşletme sahibinin adresi
     */
    private String address;

    /**
     * İşletme sahibinin kullandığı dil
     */
    private String language;

    /**
     * Bildirim tercihleri
     */
    private NotificationPreferences notificationPreferences;

    /**
     * İç içe sınıf: Bildirim tercihleri
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationPreferences {
        private boolean emailNotifications = true;
        private boolean pushNotifications = true;
        private boolean smsNotifications = false;
        private boolean marketingEmails = false;
    }
}