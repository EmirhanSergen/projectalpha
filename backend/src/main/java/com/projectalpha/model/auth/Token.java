package com.projectalpha.model.auth;

import com.projectalpha.model.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "token")
@Data
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "token_type")
    private String tokenType = "Bearer";

    @Column(name = "revoked")
    private boolean revoked = false;

    @Column(name = "expired")
    private boolean expired = false;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    public boolean isValid() {
        return !expired && !revoked && expiresAt.isAfter(OffsetDateTime.now());
    }

    // Helper method for backward compatibility if needed
    public UUID getUserId() {
        return user != null ? user.getId() : null;
    }
}