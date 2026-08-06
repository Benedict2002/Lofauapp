package com.codewithben.Lofau.admin.advertisement.controller;

import com.codewithben.Lofau.admin.advertisement.service.AdminAdvertisementService;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementDashboardResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/advertisements")
@RequiredArgsConstructor
public class AdminAdvertisementController {

    private final AdminAdvertisementService adminAdvertisementService;

    /*
     * ============================================
     * Moderation
     * ============================================
     */

    /**
     * Approve an advertisement.
     */
    @PatchMapping("/{advertisementId}/approve")
    @ResponseStatus(HttpStatus.OK)
    public AdvertisementResponse approveAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        return adminAdvertisementService
                .approveAdvertisement(advertisementId);
    }

    /**
     * Reject an advertisement.
     */
    @PatchMapping("/{advertisementId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public AdvertisementResponse rejectAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        return adminAdvertisementService
                .rejectAdvertisement(advertisementId);
    }

    /**
     * Activate an advertisement.
     */
    @PatchMapping("/{advertisementId}/activate")
    @ResponseStatus(HttpStatus.OK)
    public AdvertisementResponse activateAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        return adminAdvertisementService
                .activateAdvertisement(advertisementId);
    }

    /**
     * Deactivate an advertisement.
     */
    @PatchMapping("/{advertisementId}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public AdvertisementResponse deactivateAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        return adminAdvertisementService
                .deactivateAdvertisement(advertisementId);
    }

    /**
     * Permanently remove an advertisement.
     */
    @DeleteMapping("/{advertisementId}")
    @ResponseStatus(HttpStatus.OK)
    public AdvertisementResponse deleteAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        return adminAdvertisementService
                .deleteAdvertisement(advertisementId);
    }

    /*
     * ============================================
     * Queries
     * ============================================
     */

    /**
     * Retrieve all pending advertisements.
     */
    @GetMapping("/pending")
    public List<AdvertisementResponse> getPendingAdvertisements() {

        return adminAdvertisementService
                .getPendingAdvertisements();
    }

    /**
     * Retrieve all active advertisements.
     */
    @GetMapping("/active")
    public List<AdvertisementResponse> getActiveAdvertisements() {

        return adminAdvertisementService
                .getActiveAdvertisements();
    }

    /**
     * Retrieve every advertisement.
     */
    @GetMapping
    public List<AdvertisementResponse> getAllAdvertisements() {

        return adminAdvertisementService
                .getAllAdvertisements();
    }

    /*
     * ============================================
     * Analytics
     * ============================================
     */

    /**
     * Analytics for a single advertisement.
     */
    @GetMapping("/{advertisementId}/statistics")
    public AdvertisementStatisticsResponse getAdvertisementStatistics(
            @PathVariable UUID advertisementId
    ) {

        return adminAdvertisementService
                .getAdvertisementStatistics(advertisementId);
    }

    /**
     * Overall advertisement platform dashboard.
     */
    @GetMapping("/dashboard")
    public AdvertisementDashboardResponse getDashboard() {

        return adminAdvertisementService
                .getDashboard();
    }
}