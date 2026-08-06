package com.codewithben.Lofau.admin.analytics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdvertisementAnalyticsSummaryResponse {

    /**
     * Total campaigns.
     */
    private Long totalAdvertisements;

    /**
     * Pending approval.
     */
    private Long pendingAdvertisements;

    /**
     * Approved advertisements.
     */
    private Long approvedAdvertisements;

    /**
     * Active advertisements.
     */
    private Long activeAdvertisements;

    /**
     * Rejected advertisements.
     */
    private Long rejectedAdvertisements;

    /**
     * Total platform revenue.
     */
    private Double totalRevenue;

    /**
     * Overall click-through rate.
     */
    private Double overallCTR;
    private  Long totalClicks;
    private  Long totalImpressions;
    private  Long totalConversions;
    private Long totalRevenueSpent;
}