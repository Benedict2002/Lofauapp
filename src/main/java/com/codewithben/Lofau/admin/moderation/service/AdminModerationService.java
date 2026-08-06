package com.codewithben.Lofau.admin.moderation.service;

import java.util.UUID;

public interface AdminModerationService {

    /*
     * ==========================
     * USER MODERATION
     * ==========================
     */

    void suspendUser(UUID userId);

    void activateUser(UUID userId);

    void deactivateUser(UUID userId);

    /*
     * ==========================
     * POST MODERATION
     * ==========================
     */

    void approvePost(UUID postId);

    void deletePost(UUID postId);

    void restorePost(UUID postId);

    void pinPost(UUID postId);

    void unpinPost(UUID postId);

    /*
     * ==========================
     * ADVERTISEMENT MODERATION
     * ==========================
     */

    void approveAdvertisement(UUID advertisementId);

    void rejectAdvertisement(UUID advertisementId);

    void activateAdvertisement(UUID advertisementId);

    void deactivateAdvertisement(UUID advertisementId);

    /*
     * ==========================
     * REPORT MODERATION
     * ==========================
     */

    void resolveReport(UUID reportId);

    void dismissReport(UUID reportId);
}