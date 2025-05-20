package com.projectalpha.repository.user;

import com.projectalpha.model.user.UserProfileOwner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileOwnerRepository {

    List<UserProfileOwner> findAll();

    Optional<UserProfileOwner> findById(UUID id);

    Optional<UserProfileOwner> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserProfileOwner> findBySupabaseUid(String supabaseUid);

    Optional<UserProfileOwner> findActiveOwnerByEmail(String email);

    Optional<UserProfileOwner> findByRoleName(String roleName);

    Optional<UserProfileOwner> findByBusinessId(UUID businessId);

    Optional<UserProfileOwner> findByPhoneNumber(String phoneNumber);

    List<UserProfileOwner> findByCompanyNameContaining(String companyName);

    UserProfileOwner save(UserProfileOwner owner);

    void deleteById(UUID id);
}