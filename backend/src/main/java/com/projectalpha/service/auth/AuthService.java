package com.projectalpha.service.auth;

import com.projectalpha.dto.auth.UserDto;
import com.projectalpha.dto.auth.request.AuthRequestDto;
import com.projectalpha.dto.auth.response.AuthResponseDto;
import com.projectalpha.dto.thirdparty.SupabaseTokenResponse;
import com.projectalpha.dto.user.owner.request.OwnerRegistrationRequest;

import java.util.Optional;

// AuthService.java
public interface AuthService {
    void sendVerificationCode(String email) throws Exception;
    SupabaseTokenResponse verifyVerificationCode(String email, String token) throws Exception;
    void updateUser(String email, String newPassword, String role) throws Exception;
    void updateUser(String email, String newPassword, String role, OwnerRegistrationRequest request) throws Exception;
    SupabaseTokenResponse loginWithEmailPassword(String email, String password, String expectedRole) throws Exception;

    // Repository'deki metodlara karşılık gelen metotlar
    AuthResponseDto signUp(AuthRequestDto authRequest);
    AuthResponseDto signIn(AuthRequestDto authRequest);
    void signOut(String accessToken);
    Optional<UserDto> getCurrentUser(String accessToken);
    void resetPassword(String email);
    AuthResponseDto refreshToken(String refreshToken);
}