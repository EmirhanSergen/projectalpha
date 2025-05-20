package com.projectalpha.model.list;

import com.projectalpha.model.business.Business;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Listelerdeki iş yerlerini temsil eden ara entity
 */
@Entity
@Table(name = "custom_list_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    private CustomList customList;

    @Column(name = "business_id", nullable = false)
    private UUID businessId;

    @Column(name = "diner_id")
    private UUID dinerId;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }
}