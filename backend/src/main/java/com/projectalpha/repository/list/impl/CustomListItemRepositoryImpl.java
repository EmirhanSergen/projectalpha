package com.projectalpha.repository.list.impl;

import com.projectalpha.exception.ResourceNotFoundException;
import com.projectalpha.model.list.CustomList;
import com.projectalpha.model.list.CustomListItem;
import com.projectalpha.repository.list.CustomListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomListItemRepositoryImpl {

    private final CustomListItemRepository customListItemRepository;
    private final CustomListRepositoryImpl.JpaCustomListRepository jpaCustomListRepository;

    @Autowired
    public CustomListItemRepositoryImpl(
            CustomListItemRepository customListItemRepository,
            CustomListRepositoryImpl.JpaCustomListRepository jpaCustomListRepository) {
        this.customListItemRepository = customListItemRepository;
        this.jpaCustomListRepository = jpaCustomListRepository;
    }

    /**
     * Belirli bir listeye iş yeri ekler
     *
     * @param listId Liste ID
     * @param businessId İş yeri ID
     * @param dinerId Kullanıcı (müşteri) ID
     * @return Eklenen liste öğesi
     */
    @Transactional
    public CustomListItem addItemToList(UUID listId, UUID businessId, UUID dinerId) {
        // Listenin var olduğunu kontrol et
        CustomList customList = jpaCustomListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("Liste bulunamadı: " + listId));

        // İş yerinin zaten listede olup olmadığını kontrol et
        boolean alreadyExists = customListItemRepository.existsByCustomListIdAndBusinessId(listId, businessId);
        if (alreadyExists) {
            throw new IllegalStateException("Bu iş yeri zaten listede mevcut");
        }

        // Yeni liste öğesi oluştur
        CustomListItem listItem = new CustomListItem();
        listItem.setCustomList(customList);
        listItem.setBusinessId(businessId);
        listItem.setDinerId(dinerId);

        return customListItemRepository.save(listItem);
    }

    /**
     * Belirli bir listedeki öğeleri getirir
     *
     * @param listId Liste ID
     * @return Liste öğeleri
     */
    public List<CustomListItem> getItemsByListId(UUID listId) {
        // Listenin var olduğunu kontrol et
        if (!jpaCustomListRepository.existsById(listId)) {
            throw new ResourceNotFoundException("Liste bulunamadı: " + listId);
        }

        return customListItemRepository.findByCustomListId(listId);
    }

    /**
     * Belirli bir liste öğesini ID'ye göre getirir
     *
     * @param itemId Öğe ID
     * @return Liste öğesi
     */
    public Optional<CustomListItem> getItemById(UUID itemId) {
        return customListItemRepository.findById(itemId);
    }

    /**
     * Belirli bir listedeki belirli bir iş yerini temsil eden öğeyi getirir
     *
     * @param listId Liste ID
     * @param businessId İş yeri ID
     * @return Liste öğesi, eğer bulunursa
     */
    public Optional<CustomListItem> getItemByListAndBusiness(UUID listId, UUID businessId) {
        return customListItemRepository.findAll().stream()
                .filter(item -> item.getCustomList().getId().equals(listId) &&
                        item.getBusinessId().equals(businessId))
                .findFirst();
    }

    /**
     * Belirli bir iş yerinin bir listede olup olmadığını kontrol eder
     *
     * @param listId Liste ID
     * @param businessId İş yeri ID
     * @return İş yeri listede ise true, değilse false
     */
    public boolean isBusinessInList(UUID listId, UUID businessId) {
        return customListItemRepository.existsByCustomListIdAndBusinessId(listId, businessId);
    }

    /**
     * Bir listeden bir iş yerini kaldırır
     *
     * @param listId Liste ID
     * @param businessId İş yeri ID
     */
    @Transactional
    public void removeItemFromList(UUID listId, UUID businessId) {
        // Listenin var olduğunu kontrol et
        if (!jpaCustomListRepository.existsById(listId)) {
            throw new ResourceNotFoundException("Liste bulunamadı: " + listId);
        }

        // İş yerinin listede olup olmadığını kontrol et
        boolean exists = customListItemRepository.existsByCustomListIdAndBusinessId(listId, businessId);
        if (!exists) {
            throw new ResourceNotFoundException("Bu iş yeri listede bulunamadı");
        }

        customListItemRepository.deleteByCustomListIdAndBusinessId(listId, businessId);
    }

    /**
     * Belirli bir kullanıcının favori listesindeki öğeleri getirir
     *
     * @param dinerId Kullanıcı (müşteri) ID
     * @return Liste öğeleri
     */
    public List<CustomListItem> getItemsByDinerId(UUID dinerId) {
        return customListItemRepository.findAll().stream()
                .filter(item -> item.getDinerId() != null && item.getDinerId().equals(dinerId))
                .toList();
    }

    /**
     * Bir listeye ait tüm öğeleri siler
     *
     * @param listId Liste ID
     */
    @Transactional
    public void deleteAllItemsInList(UUID listId) {
        // Listenin var olduğunu kontrol et
        if (!jpaCustomListRepository.existsById(listId)) {
            throw new ResourceNotFoundException("Liste bulunamadı: " + listId);
        }

        customListItemRepository.deleteByCustomListId(listId);
    }
}