package com.codewithben.Lofau.admin.analytics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardSummaryResponse {

    /**
     * Total registered users.
     */
    private Long totalUsers;

    private Long activeUsers;

    private Long activePosts;
    private Long pendingReports;
    private  Long activeAdvertisements;

    /**
     * Total posts.
     */
    private Long totalPosts;

    /**
     * Total advertisements.
     */
    private Long totalAdvertisements;

    /**
     * Total reports.
     */
    private Long totalReports;

    /**
     * Total platform revenue.
     */
    private Double totalRevenue;
    private Long advertisementRevenue;
    private  double overallCTR;
}