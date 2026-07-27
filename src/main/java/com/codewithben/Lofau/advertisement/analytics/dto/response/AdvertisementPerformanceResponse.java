package com.codewithben.Lofau.advertisement.analytics.dto.response;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdvertisementPerformanceResponse {

    private Integer totalAdvertisements;

    private Integer activeAdvertisements;

    private Integer totalImpressions;

    private Integer totalClicks;

    private Integer totalConversions;

    private Double overallCTR;

    private Integer totalRevenueSpent;

}