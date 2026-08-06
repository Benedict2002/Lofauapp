package com.codewithben.Lofau.admin.analytics.service;

import com.codewithben.Lofau.admin.analytics.dto.response.AdminDashboardResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.AdvertisementAnalyticsSummaryResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.DashboardSummaryResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.ModerationAnalyticsResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.PostAnalyticsResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.ReportAnalyticsResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.UserAnalyticsResponse;

public interface AdminAnalyticsService {

    /**
     * Returns the complete admin dashboard.
     */
    AdminDashboardResponse getDashboard();

    /**
     * Returns the dashboard summary cards.
     */
    DashboardSummaryResponse getDashboardSummary();

    /**
     * Returns user analytics.
     */
    UserAnalyticsResponse getUserAnalytics();

    /**
     * Returns post analytics.
     */
    PostAnalyticsResponse getPostAnalytics();

    /**
     * Returns advertisement analytics.
     */
    AdvertisementAnalyticsSummaryResponse getAdvertisementAnalytics();

    /**
     * Returns moderation analytics.
     */
    ModerationAnalyticsResponse getModerationAnalytics();

    /**
     * Returns report analytics.
     */
    ReportAnalyticsResponse getReportAnalytics();

}