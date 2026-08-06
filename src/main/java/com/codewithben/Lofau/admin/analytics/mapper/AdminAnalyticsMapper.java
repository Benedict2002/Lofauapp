package com.codewithben.Lofau.admin.analytics.mapper;

import com.codewithben.Lofau.admin.analytics.dto.response.AdminDashboardResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.AdvertisementAnalyticsSummaryResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.DashboardSummaryResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.ModerationAnalyticsResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.PostAnalyticsResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.ReportAnalyticsResponse;
import com.codewithben.Lofau.admin.analytics.dto.response.UserAnalyticsResponse;
import org.springframework.stereotype.Component;

@Component
public class AdminAnalyticsMapper {

    /**
     * Builds the complete admin dashboard response.
     */
    public AdminDashboardResponse toDashboardResponse(

            DashboardSummaryResponse summary,

            UserAnalyticsResponse users,

            PostAnalyticsResponse posts,

            AdvertisementAnalyticsSummaryResponse advertisements,

            ModerationAnalyticsResponse moderation,

            ReportAnalyticsResponse reports

    ) {

        return AdminDashboardResponse.builder()

                .summary(summary)

                .users(users)

                .posts(posts)

                .advertisements(advertisements)

                .moderation(moderation)

                .reports(reports)

                .build();
    }

}