package com.codewithben.Lofau.event.entity;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.event.enums.EventStatus;
import com.codewithben.Lofau.group.entity.Group;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String location;

    private Double latitude;

    private Double longitude;

    @Column(nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer capacity;

    @Builder.Default
    @Column(nullable = false)
    private Integer goingCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer interestedCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer viewCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer shareCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Boolean commentsEnabled = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean cancelled = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean verified = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean featured = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private EventStatus status = EventStatus.UPCOMING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    private LocalDateTime createdAt;
    @Builder.Default
    private Integer likeCount = 0;
    private Integer commentCount;






    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = createdAt;

        if (likeCount == null) {
            likeCount = 0;
        }

        if (commentCount == null) {
            commentCount = 0;
        }

        if (viewCount == null) {
            viewCount = 0;
        }

        if (shareCount == null) {
            shareCount = 0;
        }

        if (goingCount == null) {
            goingCount = 0;
        }

        if (interestedCount == null) {
            interestedCount = 0;
        }
        if (commentsEnabled == null) {
            commentsEnabled = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }


}