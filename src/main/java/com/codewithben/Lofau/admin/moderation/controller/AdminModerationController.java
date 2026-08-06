package com.codewithben.Lofau.admin.moderation.controller;

import com.codewithben.Lofau.admin.moderation.service.AdminModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/moderation")
public class AdminModerationController {

    private final AdminModerationService adminModerationService;

    /*
     * ==========================================
     * USER MODERATION
     * ==========================================
     */

    @PutMapping("/users/{userId}/suspend")
    public ResponseEntity<Void> suspendUser(
            @PathVariable UUID userId
    ) {
        adminModerationService.suspendUser(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<Void> activateUser(
            @PathVariable UUID userId
    ) {
        adminModerationService.activateUser(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(
            @PathVariable UUID userId
    ) {
        adminModerationService.deactivateUser(userId);
        return ResponseEntity.ok().build();
    }

    /*
     * ==========================================
     * POST MODERATION
     * ==========================================
     */

    @PutMapping("/posts/{postId}/approve")
    public ResponseEntity<Void> approvePost(
            @PathVariable UUID postId
    ) {
        adminModerationService.approvePost(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable UUID postId
    ) {
        adminModerationService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/posts/{postId}/restore")
    public ResponseEntity<Void> restorePost(
            @PathVariable UUID postId
    ) {
        adminModerationService.restorePost(postId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/posts/{postId}/pin")
    public ResponseEntity<Void> pinPost(
            @PathVariable UUID postId
    ) {
        adminModerationService.pinPost(postId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/posts/{postId}/unpin")
    public ResponseEntity<Void> unpinPost(
            @PathVariable UUID postId
    ) {
        adminModerationService.unpinPost(postId);
        return ResponseEntity.ok().build();
    }

    /*
     * ==========================================
     * ADVERTISEMENT MODERATION
     * ==========================================
     */

    @PutMapping("/advertisements/{advertisementId}/approve")
    public ResponseEntity<Void> approveAdvertisement(
            @PathVariable UUID advertisementId
    ) {
        adminModerationService.approveAdvertisement(advertisementId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/advertisements/{advertisementId}/reject")
    public ResponseEntity<Void> rejectAdvertisement(
            @PathVariable UUID advertisementId
    ) {
        adminModerationService.rejectAdvertisement(advertisementId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/advertisements/{advertisementId}/activate")
    public ResponseEntity<Void> activateAdvertisement(
            @PathVariable UUID advertisementId
    ) {
        adminModerationService.activateAdvertisement(advertisementId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/advertisements/{advertisementId}/deactivate")
    public ResponseEntity<Void> deactivateAdvertisement(
            @PathVariable UUID advertisementId
    ) {
        adminModerationService.deactivateAdvertisement(advertisementId);
        return ResponseEntity.ok().build();
    }

    /*
     * ==========================================
     * REPORT MODERATION
     * ==========================================
     */

    @PutMapping("/reports/{reportId}/resolve")
    public ResponseEntity<Void> resolveReport(
            @PathVariable UUID reportId
    ) {
        adminModerationService.resolveReport(reportId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reports/{reportId}/dismiss")
    public ResponseEntity<Void> dismissReport(
            @PathVariable UUID reportId
    ) {
        adminModerationService.dismissReport(reportId);
        return ResponseEntity.ok().build();
    }

}