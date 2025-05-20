package com.projectalpha.repository.list;

import com.projectalpha.model.business.Business;
import com.projectalpha.model.list.CustomListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CustomListItemRepository extends JpaRepository<CustomListItem, UUID> {

    List<CustomListItem> findByCustomListId(UUID listId);

    @Query("SELECT cli.businessId FROM CustomListItem cli WHERE cli.customList.id = :listId")
    List<UUID> findBusinessIdsByListId(@Param("listId") UUID listId);

    @Modifying
    @Query("DELETE FROM CustomListItem cli WHERE cli.customList.id = :listId AND cli.businessId = :businessId")
    void deleteByCustomListIdAndBusinessId(@Param("listId") UUID listId, @Param("businessId") UUID businessId);

    boolean existsByCustomListIdAndBusinessId(UUID listId, UUID businessId);

    @Modifying
    @Query("DELETE FROM CustomListItem cli WHERE cli.customList.id = :listId")
    void deleteByCustomListId(@Param("listId") UUID listId);
}