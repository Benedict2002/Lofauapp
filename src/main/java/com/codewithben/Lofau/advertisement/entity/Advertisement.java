package com.codewithben.Lofau.advertisement.entity;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import com.codewithben.Lofau.advertisement.enums.AdvertisementType;
import com.codewithben.Lofau.media.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "advertisements")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /*
     * Advertiser
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertiser_id", nullable = false)
    private User advertiser;

    /*
     * Advertisement Details
     */
    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String websiteUrl;

    @Column(length = 100)
    private String callToAction;

    /*
     * Advertisement Configuration
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdvertisementType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdvertisementPlacement placement;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private AdvertisementStatus status = AdvertisementStatus.DRAFT;

    /*
     * Campaign Priority
     */
    @Builder.Default
    @Column(nullable = false)
    private Integer priority = 0;

    /*
     * Analytics
     */
    @Builder.Default
    @Column(nullable = false)
    private Integer impressions = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer clicks = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer saves = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer shares = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer conversions = 0;

    /*
     * Budget
     */
    @Builder.Default
    @Column(nullable = false)
    private Integer dailyLimit = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer dailySpent = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer totalBudget = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer spentBudget = 0;

    /*
     * Campaign State
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean approved = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean deleted = false;

    /*
     * Campaign Duration
     */
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    /*
     * Audit
     */
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(nullable = false)
    private Boolean paused = false;

    private String mediaUrl;

    private String mediaPublicId;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;



    /*
     * Computed Fields
     */


    public Boolean isBudgetExhausted() {
        return getRemainingBudget() <= 0;
    }

    @Transient
    public Integer getRemainingBudget() {
        return totalBudget - spentBudget;
    }

    @Transient
    public Boolean getPaused() {

        if (dailyLimit <= 0) {
            return false;
        }

        return dailySpent >= dailyLimit;
    }

    @Transient
    public Double getCtr() {

        if (impressions == 0) {
            return 0D;
        }

        return (double) clicks / impressions;
    }

    @Transient
    public Double getConversionRate() {

        if (clicks == 0) {
            return 0D;
        }

        return (double) conversions / clicks;
    }

    @Transient
    public Boolean isExpired() {

        return endDate != null &&
                endDate.isBefore(LocalDateTime.now());
    }

    @Transient
    public Boolean hasStarted() {

        return startDate == null ||
                !startDate.isAfter(LocalDateTime.now());
    }

    /*
     * Entity Lifecycle
     */

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = createdAt;

        if (status == null)
            status = AdvertisementStatus.DRAFT;

        if (priority == null)
            priority = 0;

        if (impressions == null)
            impressions = 0;

        if (clicks == null)
            clicks = 0;

        if (shares == null)
            shares = 0;

        if (saves == null)
            saves = 0;

        if (conversions == null)
            conversions = 0;

        if (dailyLimit == null)
            dailyLimit = 0;

        if (dailySpent == null)
            dailySpent = 0;

        if (totalBudget == null)
            totalBudget = 0;

        if (spentBudget == null)
            spentBudget = 0;

        if (active == null)
            active = true;

        if (approved == null)
            approved = false;

        if (deleted == null)
            deleted = false;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}