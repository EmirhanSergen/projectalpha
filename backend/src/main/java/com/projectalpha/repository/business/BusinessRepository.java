package com.projectalpha.repository.business;

import com.projectalpha.dto.business.BusinessDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessRepository {
    /**
     * Tüm işletmeleri listeler
     */
    List<BusinessDTO> findAll();

    /**
     * ID'ye göre işletme bulur
     */
    Optional<BusinessDTO> findById(UUID id);

    /**
     * Metin aramasına göre işletmeleri bulur
     */
    List<BusinessDTO> search(String query);
}