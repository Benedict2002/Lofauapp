package com.codewithben.Lofau.admin.analytics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModerationAnalyticsResponse {

    /**
     * Suspended users.
     */
    private Long usersSuspended;

    /**
     * Deleted posts.
     */
    private  Long postsDeleted;

    /**
     * Resolved reports.
     */
    private Long reportsResolved;

    private Long postsRestored;

    /**
     * Dismissed reports.
     */
    private Long dismissedReports;

    /**
     * Rejected advertisements.
     */
    private  Long advertisementsRejected;

    private Long advertisementsApproved;

}