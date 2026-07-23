package com.codewithben.Lofau.notification.entity;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.notification.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /*
     * Recipient
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    /*
     * User that triggered the notification
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private User actor;

    /*
     * Notification Type
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    /*
     * Related Object
     * (Post, Comment, Group, Event...)
     */
    @Column(nullable = false)
    private UUID referenceId;

    /*
     * Optional deep-link route
     * Example:
     * /posts/{id}
     * /groups/{id}
     * /events/{id}
     */
    private String actionUrl;

    /*
     * Notification text
     */
    @Column(nullable = false, length = 500)
    private String message;

    @Builder.Default
    private Boolean read = false;

    /*
     * Optional preview image owner
     */
    private UUID previewOwnerId;

    @Enumerated(EnumType.STRING)
    private OwnerType previewOwnerType;

    private LocalDateTime readAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    public void prePersist() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (read == null) {
            read = false;
        }
    }
}