package com.projectalpha.mapper;

import com.projectalpha.dto.user.owner.profile.OwnerUserProfile;
import com.projectalpha.dto.user.owner.response.OwnerLoginResponse;
import com.projectalpha.model.business.Business;
import com.projectalpha.model.user.UserProfileOwner;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * UserProfileOwner entity ve ilgili DTO'lar arasında dönüşüm sağlayan mapper sınıfı
 */
@Component
public class OwnerMapper {

    /**
     * UserProfileOwner entity'sini OwnerLoginResponse DTO'suna dönüştürür
     *
     * @param owner dönüştürülecek entity
     * @return OwnerLoginResponse nesnesi
     */
    public OwnerLoginResponse toLoginResponse(UserProfileOwner owner) {
        if (owner == null) {
            return null;
        }

        OwnerLoginResponse response = new OwnerLoginResponse();
        response.setId(owner.getId());
        response.setUsername(owner.getUsername());
        response.setEmail(owner.getEmail());
        response.setFirstName(owner.getFirstName());
        response.setLastName(owner.getLastName());
        response.setPhoneNumber(owner.getPhoneNumber());
        response.setCompanyName(owner.getCompanyName());
        response.setTaxId(owner.getTaxId());

        // İşletme bilgilerini ekle
        if (owner.getBusinesses() != null && !owner.getBusinesses().isEmpty()) {
            List<UUID> businessIds = owner.getBusinesses().stream()
                    .map(Business::getId)
                    .collect(Collectors.toList());
            response.setBusinessIds(businessIds);
        } else {
            response.setBusinessIds(Collections.emptyList());
        }

        response.setCreatedAt(owner.getCreatedAt());
        response.setUpdatedAt(owner.getUpdatedAt());

        return response;
    }

    /**
     * OwnerUserProfile DTO'sundaki değerleri, mevcut UserProfileOwner entity'sine aktarır
     *
     * @param profile güncellenecek verileri içeren DTO
     * @param owner güncellenecek entity
     */
    public void updateEntityFromProfile(OwnerUserProfile profile, UserProfileOwner owner) {
        if (profile == null || owner == null) {
            return;
        }

        // Sadece null olmayan değerleri güncelle
        if (profile.getFirstName() != null) {
            owner.setFirstName(profile.getFirstName());
        }

        if (profile.getLastName() != null) {
            owner.setLastName(profile.getLastName());
        }

        if (profile.getPhoneNumber() != null) {
            owner.setPhoneNumber(profile.getPhoneNumber());
        }

        if (profile.getCompanyName() != null) {
            owner.setCompanyName(profile.getCompanyName());
        }

        if (profile.getTaxId() != null) {
            owner.setTaxId(profile.getTaxId());
        }

        // Email ve kullanıcı adı değişikliği olabilir mi? Genellikle bunlar değiştirilmez
        if (profile.getEmail() != null) {
            owner.setEmail(profile.getEmail());
        }

        // Kullanıcının updated_at bilgisini güncelleme
        owner.setUpdatedAt(OffsetDateTime.now());
    }

    /**
     * UserProfileOwner entity'sinden OwnerUserProfile DTO'su oluşturur
     *
     * @param owner dönüştürülecek entity
     * @return OwnerUserProfile nesnesi
     */
    public OwnerUserProfile toUserProfile(UserProfileOwner owner) {
        if (owner == null) {
            return null;
        }

        OwnerUserProfile profile = new OwnerUserProfile();
        profile.setId(owner.getId());
        profile.setUsername(owner.getUsername());
        profile.setEmail(owner.getEmail());
        profile.setFirstName(owner.getFirstName());
        profile.setLastName(owner.getLastName());
        profile.setPhoneNumber(owner.getPhoneNumber());
        profile.setCompanyName(owner.getCompanyName());
        profile.setTaxId(owner.getTaxId());

        return profile;
    }

    /**
     * OwnerUserProfile DTO'sundan yeni bir UserProfileOwner entity'si oluşturur
     *
     * @param profile dönüştürülecek DTO
     * @return UserProfileOwner entity'si
     */
    public UserProfileOwner toEntity(OwnerUserProfile profile) {
        if (profile == null) {
            return null;
        }

        UserProfileOwner owner = new UserProfileOwner();
        owner.setUsername(profile.getUsername());
        owner.setEmail(profile.getEmail());
        owner.setFirstName(profile.getFirstName());
        owner.setLastName(profile.getLastName());
        owner.setPhoneNumber(profile.getPhoneNumber());
        owner.setCompanyName(profile.getCompanyName());
        owner.setTaxId(profile.getTaxId());
        owner.setCreatedAt(OffsetDateTime.now());
        owner.setUpdatedAt(OffsetDateTime.now());

        return owner;
    }
}