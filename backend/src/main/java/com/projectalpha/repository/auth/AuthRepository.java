package com.projectalpha.repository.auth;

import com.projectalpha.dto.auth.UserDto;
import com.projectalpha.dto.auth.request.AuthRequestDto;
import com.projectalpha.dto.auth.response.AuthResponseDto;
import com.projectalpha.dto.thirdparty.SupabaseTokenResponse;

import java.util.Optional;

public interface AuthRepository {

    AuthResponseDto signUp(AuthRequestDto authRequest);

    AuthResponseDto signIn(AuthRequestDto authRequest);

    void signOut(String accessToken);

    Optional<UserDto> getCurrentUser(String accessToken);

    void resetPassword(String email);

    AuthResponseDto refreshToken(String refreshToken);

    void sendVerificationCode(String email);

    SupabaseTokenResponse verifyToken(String email, String token);

    void updatePassword(String email, String newPassword);

}