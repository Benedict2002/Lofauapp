package com.codewithben.Lofau.advertisement.dto.request;

import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdvertisementRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String websiteUrl;

    private String callToAction;

    @NotNull
    private AdvertisementType type;

    @NotNull
    private AdvertisementPlacement placement;

    private Integer priority;

    private Integer dailyLimit;

    private Integer totalBudget;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}