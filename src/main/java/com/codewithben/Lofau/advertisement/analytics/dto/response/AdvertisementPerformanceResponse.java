package com.codewithben.Lofau.advertisement.analytics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdvertisementPerformanceResponse {

    /**
     * Total advertisements on the platform.
     */
    private Long totalAdvertisements;

    /**
     * Currently active advertisements.
     */
    private Long activeAdvertisements;

    /**
     * Total advertisement impressions.
     */
    private Long totalImpressions;

    /**
     * Total advertisement clicks.
     */
    private Long totalClicks;

    /**
     * Total successful conversions.
     */
    private Long totalConversions;

    /**
     * Overall Click-Through Rate.
     */
    private Double overallCTR;

    /**
     * Total amount spent across all advertisements.
     */
    private Long totalRevenueSpent;

}