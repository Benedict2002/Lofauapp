package com.codewithben.Lofau.advertisement.dto.response;

import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import com.codewithben.Lofau.advertisement.enums.AdvertisementType;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementResponse {

    /*
     * Advertisement Information
     */
    private UUID id;
    private String title;
    private String description;
    private String websiteUrl;
    private String callToAction;

    /*
     * Advertisement Configuration
     */
    private AdvertisementType type;
    private AdvertisementPlacement placement;
    private AdvertisementStatus status;

    /*
     * Priority & Performance
     */
    private Integer priority;
    private Integer impressions;
    private Integer clicks;

    /*
     * Budget
     */
    private Integer dailyLimit;
    private Integer totalBudget;
    private Integer spentBudget;
    private Integer remainingBudget;

    /*
     * State
     */
    private Boolean active;
    private Boolean approved;
    private Boolean paused;

    /*
     * Advertiser
     */
    private UUID advertiserId;
    private String advertiserUsername;
    private MediaResponse advertiserProfileImage;

    /*
     * Advertisement Media
     *
     * One advertisement can only have one media.
     * It may be an image, GIF or video.
     */
    private MediaResponse media;

    /*
     * Campaign Schedule
     */
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    /*
     * Audit Information
     */
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}