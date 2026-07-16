package com.codewithben.Lofau.notification.entity;

import com.codewithben.Lofau.User.model.User;
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

    /**
     * User receiving the notification
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    /**
     * User who triggered the notification
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private User actor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    /**
     * ID of the related object
     * Post ID
     * Comment ID
     * Group ID
     */
    @Column(nullable = false)
    private UUID referenceId;

    @Column(nullable =false,length =500)
    private String message;

    @Builder.Default
    private Boolean read = false;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}