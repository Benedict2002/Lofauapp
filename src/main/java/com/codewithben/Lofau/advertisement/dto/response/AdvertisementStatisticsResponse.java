package com.codewithben.Lofau.advertisement.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdvertisementStatisticsResponse {

    private Integer impressions;

    private Integer clicks;

    private Double ctr;

    private Integer totalBudget;

    private Integer spentBudget;

    private Integer remainingBudget;

}