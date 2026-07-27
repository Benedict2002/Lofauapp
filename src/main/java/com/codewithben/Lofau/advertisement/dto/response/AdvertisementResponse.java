package com.codewithben.Lofau.advertisement.dto.response;


import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import com.codewithben.Lofau.advertisement.enums.AdvertisementType;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementResponse {

    private UUID id;

    private String title;

    private String description;

    private String websiteUrl;

    private String callToAction;

    private AdvertisementType type;

    private AdvertisementPlacement placement;

    private AdvertisementStatus status;

    private Integer priority;

    private Integer impressions;

    private Integer clicks;

    private Integer dailyLimit;

    private Integer totalBudget;

    private Integer spentBudget;

    private Boolean active;

    private Boolean approved;

    // Advertiser

    private UUID advertiserId;

    private String advertiserUsername;

    private MediaResponse advertiserProfileImage;

    // Media

    private MediaResponse coverImage;

    private MediaResponse previewImage;

    private List<MediaResponse> gallery;

    private Integer mediaCount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}