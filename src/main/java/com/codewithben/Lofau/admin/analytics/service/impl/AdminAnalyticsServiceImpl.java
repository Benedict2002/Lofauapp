package com.codewithben.Lofau.admin.analytics.service.impl;

import com.codewithben.Lofau.Post.enums.PostStatus;
import com.codewithben.Lofau.Post.repository.PostRepository;
import com.codewithben.Lofau.User.domain.AccountStatus;
import com.codewithben.Lofau.admin.analytics.dto.response.*;
import com.codewithben.Lofau.admin.analytics.mapper.AdminAnalyticsMapper;
import com.codewithben.Lofau.admin.analytics.service.AdminAnalyticsService;
import com.codewithben.Lofau.admin.audit.enums.AuditAction;
import com.codewithben.Lofau.admin.audit.repository.AuditLogRepository;
import com.codewithben.Lofau.advertisement.analytics.service.AdvertisementAnalyticsService;

import com.codewithben.Lofau.report.enums.ReportStatus;
import com.codewithben.Lofau.report.repository.ReportRepository;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAnalyticsServiceImpl
        implements AdminAnalyticsService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final ReportRepository reportRepository;

    private final AdvertisementAnalyticsService advertisementAnalyticsService;

    private final AdminAnalyticsMapper adminAnalyticsMapper;
    private final AuditLogRepository auditRepository;

    @Override
    public AdminDashboardResponse getDashboard() {

        return adminAnalyticsMapper.toDashboardResponse(

                getDashboardSummary(),

                getUserAnalytics(),

                getPostAnalytics(),

                getAdvertisementAnalytics(),

                getModerationAnalytics(),

                getReportAnalytics()
        );
    }

    @Override
    public DashboardSummaryResponse getDashboardSummary() {

        var advertisement =
                advertisementAnalyticsService.getPlatformAnalytics();

        return DashboardSummaryResponse.builder()

                .totalUsers(userRepository.count())

                .activeUsers(
                        userRepository.countByAccountStatus(
                                AccountStatus.ACTIVE
                        )
                )

                .totalPosts(postRepository.count())

                .activePosts(
                        postRepository.countByStatus(
                                PostStatus.ACTIVE
                        )
                )

                .pendingReports(
                        reportRepository.countByStatus(
                                ReportStatus.PENDING
                        )
                )

                .activeAdvertisements(
                        advertisement.getActiveAdvertisements()
                )

                .advertisementRevenue(
                        advertisement.getTotalRevenueSpent()
                )

                .overallCTR(
                        advertisement.getOverallCTR()
                )

                .build();
    }

    @Override
    public UserAnalyticsResponse getUserAnalytics() {

        return UserAnalyticsResponse.builder()

                .totalUsers(userRepository.count())

                .activeUsers(userRepository.countByAccountStatus(AccountStatus.ACTIVE))

                .suspendedUsers(userRepository.countByAccountStatus(AccountStatus.SUSPENDED))

                .deactivatedUsers(userRepository.countByAccountStatus(AccountStatus.DEACTIVATED))

                .verifiedUsers(userRepository.countByVerifiedTrue())

                .unverifiedUsers(userRepository.countByVerifiedFalse())

                .build();
    }

    @Override
    public PostAnalyticsResponse getPostAnalytics() {

        return PostAnalyticsResponse.builder()

                .totalPosts(postRepository.count())

                .activePosts(postRepository.countByStatus(PostStatus.ACTIVE))

                .deletedPosts(postRepository.countByStatus(PostStatus.DELETED))

                .pendingPosts(postRepository.countByStatus(PostStatus.PENDING))

                .reportedPosts(
                        reportRepository.count()
                )

                .pinnedPosts(postRepository.countByPinnedTrue())

                .build();
    }

    @Override
    public AdvertisementAnalyticsSummaryResponse
    getAdvertisementAnalytics() {

        var analytics =
                advertisementAnalyticsService.getPlatformAnalytics();

        return AdvertisementAnalyticsSummaryResponse.builder()

                .totalAdvertisements(
                        analytics.getTotalAdvertisements()
                )

                .activeAdvertisements(
                        analytics.getActiveAdvertisements()
                )

                .totalClicks(
                        analytics.getTotalClicks()
                )

                .totalImpressions(
                        analytics.getTotalImpressions()
                )

                .totalConversions(
                        analytics.getTotalConversions()
                )

                .overallCTR(
                        analytics.getOverallCTR()
                )

                .totalRevenueSpent(
                        analytics.getTotalRevenueSpent()
                )

                .build();
    }

    @Override
    public ModerationAnalyticsResponse getModerationAnalytics() {

        return ModerationAnalyticsResponse.builder()

                .usersSuspended(
                        auditRepository.countByAction(
                                AuditAction.USER_SUSPENDED
                        )
                )

                .postsDeleted(
                        auditRepository.countByAction(
                                AuditAction.POST_DELETED
                        )
                )

                .postsRestored(
                        auditRepository.countByAction(
                                AuditAction.POST_RESTORED
                        )
                )

                .reportsResolved(
                        auditRepository.countByAction(
                                AuditAction.REPORT_RESOLVED
                        )
                )

                .advertisementsApproved(
                        auditRepository.countByAction(
                                AuditAction.ADVERTISEMENT_APPROVED
                        )
                )

                .advertisementsRejected(
                        auditRepository.countByAction(
                                AuditAction.ADVERTISEMENT_REJECTED
                        )
                )

                .build();
    }

    @Override
    public ReportAnalyticsResponse getReportAnalytics() {

        return ReportAnalyticsResponse.builder()

                .totalReports(reportRepository.count())

                .pendingReports(
                        reportRepository.countByStatus(ReportStatus.PENDING)
                )

                .resolvedReports(
                        reportRepository.countByStatus(ReportStatus.RESOLVED)
                )

                .dismissedReports(
                        reportRepository.countByStatus(ReportStatus.DISMISSED)
                )

                .build();
    }
}