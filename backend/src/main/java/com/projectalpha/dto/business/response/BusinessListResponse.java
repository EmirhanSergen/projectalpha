package com.projectalpha.dto.business.response;

import com.projectalpha.dto.business.BusinessDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessListResponse {
    private List<BusinessDto> businesses;
    private int totalCount;
    private int page;
    private int size;
}