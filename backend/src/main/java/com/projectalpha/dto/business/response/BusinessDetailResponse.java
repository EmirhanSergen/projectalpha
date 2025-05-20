package com.projectalpha.dto.business.response;

import com.projectalpha.dto.business.BusinessDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDetailResponse {
    private BusinessDto business;
    // İlave detay alanları
}