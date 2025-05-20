package com.projectalpha.model.business.settings;

import com.projectalpha.model.business.Business;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "restaurant_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "reservation_interval_minutes")
    private Integer reservationIntervalMinutes;

    @Column(name = "max_party_size")
    private Integer maxPartySize;

    @Column(name = "min_advance_hours")
    private Integer minAdvanceHours;

    @Column(name = "max_advance_days")
    private Integer maxAdvanceDays;


    @Column(name = "seating_options", columnDefinition = "jsonb")
    private String seatingOptions;

    @Column(name = "table_turn_time_minutes")
    private Integer tableTurnTimeMinutes;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}