package com.codewithben.Lofau.admin.analytics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminAnalyticsResponse {

    /*
     * ==========================
     * Users
     * ==========================
     */
    private Long totalUsers;
    private Long activeUsers;
    private Long suspendedUsers;

    /*
     * ==========================
     * Posts
     * ==========================
     */
    private Long totalPosts;
    private Long activePosts;
    private Long deletedPosts;

    /*
     * ==========================
     * Reports
     * ==========================
     */
    private Long totalReports;
    private Long pendingReports;
    private Long resolvedReports;

    /*
     * ==========================
     * Advertisements
     * ==========================
     */
    private Long totalAdvertisements;
    private Long activeAdvertisements;

    private Integer advertisementImpressions;
    private Integer advertisementClicks;
    private Integer advertisementConversions;

    /*
     * ==========================
     * Engagement
     * ==========================
     */
    private Long totalComments;
    private Long totalLikes;
    private Long totalShares;

    /*
     * ==========================
     * Revenue
     * ==========================
     */
    private Integer advertisementRevenue;

    /*
     * ==========================
     * Moderation
     * ==========================
     */
    private Long totalModerationActions;

}