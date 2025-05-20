package com.projectalpha.model.business;

import com.projectalpha.model.business.address.Address;
import com.projectalpha.model.business.photo.Photo;
import com.projectalpha.model.business.settings.RestaurantSettings;
import com.projectalpha.model.business.tag.BusinessTag;
import com.projectalpha.model.promotion.Promotion;
import com.projectalpha.model.reservation.Reservation;
import com.projectalpha.model.review.Review;
import com.projectalpha.model.user.UserProfileOwner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "business")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price_range")
    private String priceRange;

    @Column(name = "avg_rating")
    private BigDecimal avgRating;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserProfileOwner owner;

    @OneToOne(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @OneToOne(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private RestaurantSettings settings;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperatingHour> operatingHours = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BusinessTag> businessTags = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Promotion> promotions = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    // Helper method for backward compatibility if needed
    public UUID getOwnerId() {
        return owner != null ? owner.getId() : null;
    }
}