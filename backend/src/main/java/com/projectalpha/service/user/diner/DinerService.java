package com.projectalpha.service.user.diner;


import com.projectalpha.dto.business.BusinessDto;
import com.projectalpha.dto.user.diner.request.DinerUpdateRequest;
import com.projectalpha.dto.user.diner.response.DinerProfileResponse;
import com.projectalpha.dto.user.diner.response.DinerProfileResponse;
import com.projectalpha.model.business.Business;

import java.util.List;
import java.util.Optional;

public interface DinerService {
    Optional<DinerProfileResponse> getDinerProfileByUserId(String userId);
    void updateProfile(String userId, DinerUpdateRequest request);
    List<Business> getDinerFavorites(String userId);
}
