package com.codewithben.Lofau.admin.analytics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAnalyticsResponse {

    /**
     * Total registered users.
     */
    private Long totalUsers;

    /**
     * Users registered today.
     */
    private Long newUsersToday;

    /**
     * Users registered this week.
     */
    private Long newUsersThisWeek;

    /**
     * Users registered this month.
     */
    private Long newUsersThisMonth;

    /**
     * Verified users.
     */
    private Long verifiedUsers;

    /**
     * Active users.
     */
    private Long activeUsers;

    /**
     * Suspended users.
     */
    private Long suspendedUsers;

    private Long deactivatedUsers;

    private Long unverifiedUsers;
}