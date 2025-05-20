package com.projectalpha.dto.list.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.UUID;

/**
 * Müşterinin özel liste oluşturma/güncelleme isteği için DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomListRequest {

    /**
     * Listenin adı
     */
    private String name;

    /**
     * Liste açıklaması (isteğe bağlı)
     */
    private String description;

    /**
     * Listenin özel mi yoksa herkese açık mı olduğu
     */
    private boolean isPrivate = true;

    /**
     * Listeye eklenmek istenen işletmelerin ID'leri (isteğe bağlı)
     */
    private List<UUID> businessIds;
}