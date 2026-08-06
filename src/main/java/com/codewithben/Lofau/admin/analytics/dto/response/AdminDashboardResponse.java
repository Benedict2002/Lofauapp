package com.codewithben.Lofau.admin.analytics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardResponse {

    /**
     * Top dashboard summary.
     */
    private DashboardSummaryResponse summary;

    /**
     * User analytics.
     */
    private UserAnalyticsResponse users;

    /**
     * Post analytics.
     */
    private PostAnalyticsResponse posts;

    /**
     * Advertisement analytics.
     */
    private AdvertisementAnalyticsSummaryResponse advertisements;

    /**
     * Moderation analytics.
     */
    private ModerationAnalyticsResponse moderation;

    /**
     * Report analytics.
     */
    private ReportAnalyticsResponse reports;
}