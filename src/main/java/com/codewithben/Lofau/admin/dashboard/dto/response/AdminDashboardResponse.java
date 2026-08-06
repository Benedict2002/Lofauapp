package com.codewithben.Lofau.admin.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {

    /*
     * =====================================================
     * USER STATISTICS
     * =====================================================
     */
    private Long totalUsers;

    private Long activeUsers;

    private Long verifiedUsers;

    private Long suspendedUsers;

    private Long deactivatedUsers;

    /*
     * =====================================================
     * POST STATISTICS
     * =====================================================
     */
    private Long totalPosts;

    private Long activePosts;

    private Long resolvedPosts;

    private Long deletedPosts;

    private Long lostPosts;

    private Long foundPosts;

    /*
     * =====================================================
     * ADVERTISEMENT STATISTICS
     * =====================================================
     */
    private Long totalAdvertisements;

    private Long activeAdvertisements;

    private Long pendingAdvertisements;

    private Long approvedAdvertisements;

    private Long rejectedAdvertisements;

    private Long pausedAdvertisements;

    private Long expiredAdvertisements;

    private Long completedAdvertisements;

    /*
     * =====================================================
     * REPORT STATISTICS
     * =====================================================
     */
    private Long pendingReports;

    /*
     * =====================================================
     * STORAGE
     * =====================================================
     */
    private Long totalStorageUsed;
}