package com.codewithben.Lofau.admin.dashboard.service.impl;

import com.codewithben.Lofau.Post.enums.PostStatus;
import com.codewithben.Lofau.Post.enums.PostType;
import com.codewithben.Lofau.Post.repository.PostRepository;

import com.codewithben.Lofau.User.domain.AccountStatus;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.admin.dashboard.dto.response.AdminDashboardResponse;
import com.codewithben.Lofau.admin.dashboard.service.AdminDashboardService;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import com.codewithben.Lofau.advertisement.repository.AdvertisementRepository;
import com.codewithben.Lofau.media.repository.MediaRepository;
import com.codewithben.Lofau.report.enums.ReportStatus;
import com.codewithben.Lofau.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final AdvertisementRepository advertisementRepository;

    private final ReportRepository reportRepository;

    private final MediaRepository mediaRepository;

    @Override
    public AdminDashboardResponse getDashboard() {

        return AdminDashboardResponse.builder()

                // =====================================================
                // USER STATISTICS
                // =====================================================

                .totalUsers(userRepository.count())

                .activeUsers(
                        userRepository.countByAccountStatus(AccountStatus.ACTIVE)
                )

                .verifiedUsers(
                        userRepository.countByVerifiedTrue()
                )

                .suspendedUsers(
                        userRepository.countByAccountStatus(AccountStatus.SUSPENDED)
                )

                .deactivatedUsers(
                        userRepository.countByAccountStatus(AccountStatus.DEACTIVATED)
                )

                // =====================================================
                // POST STATISTICS
                // =====================================================

                .totalPosts(
                        postRepository.count()
                )

                .activePosts(
                        postRepository.countByStatus(PostStatus.ACTIVE)
                )

                .resolvedPosts(
                        postRepository.countByStatus(PostStatus.RESOLVED)
                )

                .deletedPosts(
                        postRepository.countByStatus(PostStatus.DELETED)
                )

                .lostPosts(
                        postRepository.countByPostType(PostType.LOST)
                )

                .foundPosts(
                        postRepository.countByPostType(PostType.FOUND)
                )

                // =====================================================
                // ADVERTISEMENT STATISTICS
                // =====================================================

                .totalAdvertisements(
                        advertisementRepository.count()
                )

                .activeAdvertisements(
                        advertisementRepository.countByActiveTrue()
                )

                .pendingAdvertisements(
                        advertisementRepository.countByStatus(
                                AdvertisementStatus.PENDING_APPROVAL
                        )
                )

                .approvedAdvertisements(
                        advertisementRepository.countByApprovedTrue()
                )

                .rejectedAdvertisements(
                        advertisementRepository.countByStatus(
                                AdvertisementStatus.REJECTED
                        )
                )

                .pausedAdvertisements(
                        advertisementRepository.countByPausedTrue()
                )

                .expiredAdvertisements(
                        advertisementRepository.countByStatus(
                                AdvertisementStatus.EXPIRED
                        )
                )

                .completedAdvertisements(
                        advertisementRepository.countByStatus(
                                AdvertisementStatus.COMPLETED
                        )
                )

                // =====================================================
                // REPORTS
                // =====================================================

                .pendingReports(
                        reportRepository.countByStatus(
                                ReportStatus.PENDING
                        )
                )

                // =====================================================
                // STORAGE
                // =====================================================

                .totalStorageUsed(
                        mediaRepository.getTotalStorageUsed()
                )

                .build();
    }
}