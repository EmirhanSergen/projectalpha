package com.projectalpha.model.list;

import com.projectalpha.model.user.diner.CustomListItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Kullanıcıların oluşturdukları özel listeleri temsil eden entity
 */
@Entity
@Table(name = "custom_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "user_profile_diner_id")
    private UUID userProfileDinerId;

    @Column(name = "like_counter")
    private Integer likeCounter = 0;

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "customList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CustomListItem> items;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}