package com.codewithben.Lofau.admin.analytics.controller;

import com.codewithben.Lofau.admin.analytics.dto.response.AdminDashboardResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.AdvertisementAnalyticsSummaryResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.DashboardSummaryResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.ModerationAnalyticsResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.PostAnalyticsResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.ReportAnalyticsResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.UserAnalyticsResponse;
import com.codewithben.Lofau.admin.analytics.service.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@RequiredArgsConstructor
public class AdminAnalyticsController {

    private final AdminAnalyticsService adminAnalyticsService;

    /**
     * ============================================
     * Complete Admin Dashboard
     * ============================================
     */
    @GetMapping("/dashboard")
    public AdminDashboardResponse getDashboard() {

        return adminAnalyticsService.getDashboard();
    }

    /**
     * ============================================
     * Dashboard Summary
     * ============================================
     */
    @GetMapping("/summary")
    public DashboardSummaryResponse getDashboardSummary() {

        return adminAnalyticsService.getDashboardSummary();
    }

    /**
     * ============================================
     * User Analytics
     * ============================================
     */
    @GetMapping("/users")
    public UserAnalyticsResponse getUserAnalytics() {

        return adminAnalyticsService.getUserAnalytics();
    }

    /**
     * ============================================
     * Post Analytics
     * ============================================
     */
    @GetMapping("/posts")
    public PostAnalyticsResponse getPostAnalytics() {

        return adminAnalyticsService.getPostAnalytics();
    }

    /**
     * ============================================
     * Advertisement Analytics
     * ============================================
     */
    @GetMapping("/advertisements")
    public AdvertisementAnalyticsSummaryResponse getAdvertisementAnalytics() {

        return adminAnalyticsService.getAdvertisementAnalytics();
    }

    /**
     * ============================================
     * Moderation Analytics
     * ============================================
     */
    @GetMapping("/moderation")
    public ModerationAnalyticsResponse getModerationAnalytics() {

        return adminAnalyticsService.getModerationAnalytics();
    }

    /**
     * ============================================
     * Report Analytics
     * ============================================
     */
    @GetMapping("/reports")
    public ReportAnalyticsResponse getReportAnalytics() {

        return adminAnalyticsService.getReportAnalytics();
    }
}