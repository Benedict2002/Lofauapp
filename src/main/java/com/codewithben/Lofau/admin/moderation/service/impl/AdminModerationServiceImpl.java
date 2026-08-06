package com.codewithben.Lofau.admin.moderation.service.impl;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.enums.PostStatus;
import com.codewithben.Lofau.Post.repository.PostRepository;
import com.codewithben.Lofau.User.domain.AccountStatus;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.admin.audit.enums.AuditAction;
import com.codewithben.Lofau.admin.audit.enums.AuditEntityType;
import com.codewithben.Lofau.admin.audit.service.AuditService;
import com.codewithben.Lofau.admin.moderation.service.AdminModerationService;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import com.codewithben.Lofau.advertisement.repository.AdvertisementRepository;
import com.codewithben.Lofau.report.entity.Report;
import com.codewithben.Lofau.report.enums.ReportStatus;
import com.codewithben.Lofau.report.repository.ReportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminModerationServiceImpl
        implements AdminModerationService {

    private final AuditService auditService;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final AdvertisementRepository advertisementRepository;

    private final ReportRepository reportRepository;

    /*
     * =====================================
     * USER MODERATION
     * =====================================
     */

    @Override
    public void suspendUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found."));

        user.setAccountStatus(AccountStatus.SUSPENDED);

        userRepository.save(user);
        auditService.log(
                AuditAction.USER_SUSPENDED,
                AuditEntityType.USER,
                user.getId(),
                user.getEmail(),
                "User account suspended.",
                null
        );
    }

    @Override
    public void activateUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found."));

        user.setAccountStatus(AccountStatus.ACTIVE);

        userRepository.save(user);
        auditService.log(
                AuditAction.USER_ACTIVATED,
                AuditEntityType.USER,
                user.getId(),
                user.getEmail(),
                "User account activated.",
                null
        );
    }

    @Override
    public void deactivateUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found."));

        user.setAccountStatus(AccountStatus.DEACTIVATED);

        userRepository.save(user);
        auditService.log(
                AuditAction.USER_DEACTIVATED,
                AuditEntityType.USER,
                user.getId(),
                user.getEmail(),
                "User account deactivated.",
                null
        );
    }

    /*
     * =====================================
     * POST MODERATION
     * =====================================
     */

    @Override
    public void approvePost(UUID postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Post not found."));

        post.setApproved(true);

        postRepository.save(post);
        auditService.log(
                AuditAction.POST_APPROVED,
                AuditEntityType.POST,
                post.getId(),
                post.getTitle(),
                "Post approved.",
                null
        );
    }

    @Override
    public void deletePost(UUID postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Post not found."));

        post.setDeleted(true);
        post.setDeletedAt(LocalDateTime.now());
        post.setStatus(PostStatus.DELETED);

        postRepository.save(post);
        auditService.log(
                AuditAction.POST_DELETED,
                AuditEntityType.POST,
                post.getId(),
                post.getTitle(),
                "Post deleted by administrator.",
                null
        );
    }

    @Override
    public void restorePost(UUID postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Post not found."));

        post.setDeleted(false);
        post.setDeletedAt(null);
        post.setStatus(PostStatus.ACTIVE);

        postRepository.save(post);
        auditService.log(
                AuditAction.POST_RESTORED,
                AuditEntityType.POST,
                post.getId(),
                post.getTitle(),
                "Post restored.",
                null
        );
    }

    @Override
    public void pinPost(UUID postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Post not found."));

        post.setPinned(true);
        post.setPinnedAt(LocalDateTime.now());

        postRepository.save(post);
        auditService.log(
                AuditAction.POST_PINNED,
                AuditEntityType.POST,
                post.getId(),
                post.getTitle(),
                "Post pinned.",
                null
        );
    }

    @Override
    public void unpinPost(UUID postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Post not found."));

        post.setPinned(false);
        post.setPinnedAt(null);

        postRepository.save(post);
        auditService.log(
                AuditAction.POST_UNPINNED,
                AuditEntityType.POST,
                post.getId(),
                post.getTitle(),
                "Post unpinned.",
                null
        );
    }

    /*
     * =====================================
     * ADVERTISEMENT MODERATION
     * =====================================
     */

    @Override
    public void approveAdvertisement(UUID advertisementId) {

        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Advertisement not found."));

        advertisement.setApproved(true);
        advertisement.setStatus(AdvertisementStatus.ACTIVE);
        advertisement.setActive(true);

        advertisementRepository.save(advertisement);
        auditService.log(
                AuditAction.ADVERTISEMENT_APPROVED,
                AuditEntityType.ADVERTISEMENT,
                advertisement.getId(),
                advertisement.getTitle(),
                "Advertisement approved.",
                null
        );
    }

    @Override
    public void rejectAdvertisement(UUID advertisementId) {

        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Advertisement not found."));

        advertisement.setApproved(false);
        advertisement.setStatus(AdvertisementStatus.REJECTED);
        advertisement.setActive(false);

        advertisementRepository.save(advertisement);
        auditService.log(
                AuditAction.ADVERTISEMENT_REJECTED,
                AuditEntityType.ADVERTISEMENT,
                advertisement.getId(),
                advertisement.getTitle(),
                "Advertisement rejected.",
                null
        );
    }

    @Override
    public void activateAdvertisement(UUID advertisementId) {

        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Advertisement not found."));

        advertisement.setStatus(AdvertisementStatus.ACTIVE);
        advertisement.setActive(true);

        advertisementRepository.save(advertisement);
        auditService.log(
                AuditAction.ADVERTISEMENT_ACTIVATED,
                AuditEntityType.ADVERTISEMENT,
                advertisement.getId(),
                advertisement.getTitle(),
                "Advertisement activated.",
                null
        );
    }

    @Override
    public void deactivateAdvertisement(UUID advertisementId) {

        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Advertisement not found."));

        advertisement.setStatus(AdvertisementStatus.INACTIVE);
        advertisement.setActive(false);

        advertisementRepository.save(advertisement);
        auditService.log(
                AuditAction.ADVERTISEMENT_DEACTIVATED,
                AuditEntityType.ADVERTISEMENT,
                advertisement.getId(),
                advertisement.getTitle(),
                "Advertisement deactivated.",
                null
        );
    }

    /*
     * =====================================
     * REPORT MODERATION
     * =====================================
     */

    @Override
    public void resolveReport(UUID reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Report not found."));

        report.setStatus(ReportStatus.RESOLVED);
        report.setReviewedAt(LocalDateTime.now());

        reportRepository.save(report);
        auditService.log(
                AuditAction.REPORT_RESOLVED,
                AuditEntityType.REPORT,
                report.getId(),
                report.getReason().name(),
                "Report resolved.",
                null
        );
    }

    @Override
    public void dismissReport(UUID reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Report not found."));

        report.setStatus(ReportStatus.DISMISSED);
        report.setReviewedAt(LocalDateTime.now());

        reportRepository.save(report);
        auditService.log(
                AuditAction.REPORT_DISMISSED,
                AuditEntityType.REPORT,
                report.getId(),
                report.getReason().name(),
                "Report dismissed.",
                null
        );
    }
}