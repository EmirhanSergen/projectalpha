package com.projectalpha.service.user;

import com.projectalpha.dto.user.diner.request.DinerUpdateRequest;
import com.projectalpha.dto.user.diner.response.DinerProfileResponse;
import com.projectalpha.dto.user.owner.profile.OwnerUserProfile;
import com.projectalpha.dto.user.owner.response.OwnerLoginResponse;
import com.projectalpha.mapper.DinerMapper;
import com.projectalpha.mapper.OwnerMapper;
import com.projectalpha.model.business.Business;
import com.projectalpha.model.user.UserProfileDiner;
import com.projectalpha.model.user.UserProfileOwner;
import com.projectalpha.repository.user.UserProfileDinerRepository;
import com.projectalpha.repository.user.UserProfileOwnerRepository;
import com.projectalpha.service.user.diner.DinerService;
import com.projectalpha.service.user.owner.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Kullanıcı servisi - Hem müşteri (diner) hem de işletme sahibi (owner) işlemlerini yönetir.
 */
@Service
@Transactional
public class UserService implements DinerService, OwnerService {
    private final UserProfileDinerRepository dinerRepository;
    private final UserProfileOwnerRepository ownerRepository;
    private final DinerMapper dinerMapper;
    private final OwnerMapper ownerMapper;

    @Autowired
    public UserService(
            UserProfileDinerRepository dinerRepository,
            UserProfileOwnerRepository ownerRepository,
            DinerMapper dinerMapper,
            OwnerMapper ownerMapper) {
        this.dinerRepository = dinerRepository;
        this.ownerRepository = ownerRepository;
        this.dinerMapper = dinerMapper;
        this.ownerMapper = ownerMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DinerProfileResponse> getDinerProfileByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı ID boş olamaz");
        }

        try {
            UUID userUuid = UUID.fromString(userId);
            Optional<UserProfileDiner> dinerOptional = dinerRepository.findById(userUuid);

            // Entity'i DTO'ya dönüştürme
            return dinerOptional.map(dinerMapper::toDto);
        } catch (IllegalArgumentException e) {
            // UUID dönüşümünde hata olursa
            throw new IllegalArgumentException("Geçersiz kullanıcı ID formatı: " + userId, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OwnerLoginResponse> getOwnerProfileByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı ID boş olamaz");
        }

        try {
            UUID userUuid = UUID.fromString(userId);
            Optional<UserProfileOwner> ownerOptional = ownerRepository.findById(userUuid);

            // Entity'i DTO'ya dönüştürme
            return ownerOptional.map(ownerMapper::toLoginResponse);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Geçersiz kullanıcı ID formatı: " + userId, e);
        }
    }

    @Override
    @Transactional
    public void updateProfile(String userId, DinerUpdateRequest request) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı ID boş olamaz");
        }
        if (request == null) {
            throw new IllegalArgumentException("Güncelleme isteği boş olamaz");
        }

        try {
            UUID userUuid = UUID.fromString(userId);

            // Kullanıcıyı bul
            UserProfileDiner diner = dinerRepository.findById(userUuid)
                    .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + userId));

            // DTO'dan gelen verilerle entity'i güncelle
            dinerMapper.updateEntityFromDto(request, diner);

            // Güncellenmiş entity'i kaydet
            dinerRepository.save(diner);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Geçersiz kullanıcı ID formatı veya kullanıcı bulunamadı: " + userId, e);
        }
    }

    @Override
    @Transactional
    public void updateProfile(String userId, OwnerUserProfile profile) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı ID boş olamaz");
        }
        if (profile == null) {
            throw new IllegalArgumentException("Profil boş olamaz");
        }

        try {
            UUID userUuid = UUID.fromString(userId);

            // Kullanıcıyı bul
            UserProfileOwner owner = ownerRepository.findById(userUuid)
                    .orElseThrow(() -> new IllegalArgumentException("İşletme sahibi bulunamadı: " + userId));

            // DTO'dan gelen verilerle entity'i güncelle
            ownerMapper.updateEntityFromProfile(profile, owner);

            // Güncellenmiş entity'i kaydet
            ownerRepository.save(owner);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Geçersiz kullanıcı ID formatı veya kullanıcı bulunamadı: " + userId, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Business> getDinerFavorites(String userId) {
        // TODO: Bu metod ileride implemente edilecek
        return new ArrayList<>(); // Şimdilik boş liste döndür
    }
    /*
    @Override
    @Transactional(readOnly = true)
    public List<Business> getDinerFavorites(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Kullanıcı ID boş olamaz");
        }

        try {
            UUID userUuid = UUID.fromString(userId);
            return favoritesRepository.getDinerFavorites(userUuid);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Geçersiz kullanıcı ID formatı: " + userId, e);
        }
    }*/
}