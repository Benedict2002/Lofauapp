package com.codewithben.Lofau.advertisement.analytics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdvertisementAnalyticsResponse {

    private Integer impressions;

    private Integer clicks;

    private Integer shares;

    private Integer saves;

    private Integer conversions;

    private Double ctr;

    private Double conversionRate;

    private Integer spentBudget;

    private Integer remainingBudget;

}