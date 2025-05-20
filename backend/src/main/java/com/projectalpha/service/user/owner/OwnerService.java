package com.projectalpha.service.user.owner;

import com.projectalpha.dto.user.owner.response.OwnerLoginResponse;
import com.projectalpha.dto.user.owner.profile.OwnerUserProfile;

import java.util.Optional;

public interface OwnerService {
    Optional<OwnerLoginResponse> getOwnerProfileByUserId(String userId);
    void updateProfile(String userId, OwnerUserProfile profile);
}
