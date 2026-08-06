package com.codewithben.Lofau.admin.analytics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportAnalyticsResponse {

    /**
     * Pending reports.
     */
    private Long pendingReports;

    /**
     * Resolved reports.
     */
    private Long resolvedReports;

    /**
     * Dismissed reports.
     */
    private Long dismissedReports;

    /**
     * Total reports.
     */
    private Long totalReports;
}