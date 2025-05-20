package com.projectalpha.model.notification;

import com.projectalpha.model.user.UserProfile;
import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private NotificationType type;


    @Column(name = "payload", columnDefinition = "jsonb")
    private String payload;

    // Tek bir user ilişkisi ekliyoruz:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserProfile user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}