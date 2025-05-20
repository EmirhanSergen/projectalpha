package com.projectalpha.repository.user;

import com.projectalpha.model.user.UserProfileDiner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileDinerRepository {

    List<UserProfileDiner> findAll();

    Optional<UserProfileDiner> findById(UUID id);

    Optional<UserProfileDiner> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserProfileDiner> findBySupabaseUid(String supabaseUid);

    Optional<UserProfileDiner> findActiveDinerByEmail(String email);

    List<UserProfileDiner> findByNameContaining(String name);

    UserProfileDiner save(UserProfileDiner diner);

    void deleteById(UUID id);
}