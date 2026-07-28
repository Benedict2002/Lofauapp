package com.codewithben.Lofau.advertisement.dto.request;

import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import com.codewithben.Lofau.advertisement.enums.AdvertisementType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdvertisementRequest {

    private String title;

    private String description;

    private String websiteUrl;

    private String callToAction;

    private AdvertisementType type;

    private AdvertisementPlacement placement;

    private AdvertisementStatus status;

    private Integer priority;

    private Integer dailyLimit;

    private Integer totalBudget;

    private Boolean active;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
    /**
     * Optional replacement media.
     */
    private MultipartFile media;
}
