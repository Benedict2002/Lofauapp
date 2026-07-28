package com.codewithben.Lofau.advertisement.controller;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.advertisement.dto.request.CreateAdvertisementRequest;
import com.codewithben.Lofau.advertisement.dto.request.UpdateAdvertisementRequest;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementDashboardResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementStatisticsResponse;
import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;
import com.codewithben.Lofau.advertisement.feed.AdvertisementFeedService;
import com.codewithben.Lofau.advertisement.service.AdvertisementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final AdvertisementFeedService advertisementFeedService;
    private final UserRepository userRepository;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found."));
    }

    /**
     * Creates a new advertisement together with its media.
     *
     * Supported media:
     * - Image
     * - GIF
     * - Video
     *
     * The request must be sent as multipart/form-data.
     */
    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertisementResponse createAdvertisement(

            @Valid
            @ModelAttribute CreateAdvertisementRequest request

    ) throws IOException {

        return advertisementService.createAdvertisement(request);
    }

    /**
     * Updates an existing advertisement together with its media.
     *
     * If a new media file is supplied,
     * the previous media will be replaced.
     */
    @PutMapping(
            value = "/{advertisementId}",
            consumes = "multipart/form-data"
    )
    public AdvertisementResponse updateAdvertisement(

            @PathVariable UUID advertisementId,

            @Valid
            @ModelAttribute UpdateAdvertisementRequest request

    ) throws IOException {

        return advertisementService.updateAdvertisement(
                advertisementId,
                request
        );
    }

    /**
     * Retrieves a single advertisement by its ID.
     */
    @GetMapping("/{advertisementId}")
    public AdvertisementResponse getAdvertisement(
            @PathVariable UUID advertisementId
    ) {
        return advertisementService.getAdvertisement(
                advertisementId
        );
    }

    /**
     * Retrieves all advertisements.
     */
    @GetMapping
    public List<AdvertisementResponse> getAdvertisements() {
        return advertisementService.getAdvertisements();
    }

    /**
     * Retrieves all advertisements created
     * by the currently authenticated user.
     */
    @GetMapping("/mine")
    public List<AdvertisementResponse> getMyAdvertisements() {
        return advertisementService.getMyAdvertisements();
    }

    /**
     * Retrieves advertisements belonging
     * to a specific placement.
     */
    @GetMapping("/placement/{placement}")
    public List<AdvertisementResponse> getByPlacement(
            @PathVariable AdvertisementPlacement placement
    ) {
        return advertisementService.getAdvertisementsByPlacement(
                placement.name()
        );
    }

    /**
     * Approves an advertisement.
     * Intended for administrators.
     */
    @PatchMapping("/{advertisementId}/approve")
    public AdvertisementResponse approveAdvertisement(
            @PathVariable UUID advertisementId
    ) {
        return advertisementService.approveAdvertisement(
                advertisementId
        );
    }

    /**
     * Rejects an advertisement.
     * Intended for administrators.
     */
    @PatchMapping("/{advertisementId}/reject")
    public AdvertisementResponse rejectAdvertisement(
            @PathVariable UUID advertisementId
    ) {
        return advertisementService.rejectAdvertisement(
                advertisementId
        );
    }

    /**
     * Activates an approved advertisement.
     */
    @PatchMapping("/{advertisementId}/activate")
    public AdvertisementResponse activateAdvertisement(
            @PathVariable UUID advertisementId
    ) {
        return advertisementService.activateAdvertisement(
                advertisementId
        );
    }

    /**
     * Deactivates an advertisement.
     */
    @PatchMapping("/{advertisementId}/deactivate")
    public AdvertisementResponse deactivateAdvertisement(
            @PathVariable UUID advertisementId
    ) {
        return advertisementService.deactivateAdvertisement(
                advertisementId
        );
    }

    /**
     * Temporarily pauses an active advertisement.
     */
    @PatchMapping("/{advertisementId}/pause")
    public AdvertisementResponse pauseAdvertisement(
            @PathVariable UUID advertisementId
    ) {
        return advertisementService.pauseAdvertisement(
                advertisementId
        );
    }

    /**
     * Resumes a previously paused advertisement.
     */
    @PatchMapping("/{advertisementId}/resume")
    public AdvertisementResponse resumeAdvertisement(
            @PathVariable UUID advertisementId
    ) {
        return advertisementService.resumeAdvertisement(
                advertisementId
        );
    }

    /**
     * Soft deletes an advertisement.
     * The advertisement remains in the database
     * but is no longer visible.
     */
    @DeleteMapping("/{advertisementId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdvertisement(
            @PathVariable UUID advertisementId
    ) {
        advertisementService.deleteAdvertisement(
                advertisementId
        );
    }

    /**
     * Returns the highest-ranked promoted advertisement
     * for the specified placement.
     */
    @GetMapping("/feed/{placement}")
    public AdvertisementResponse getPromotedAdvertisement(
            @PathVariable AdvertisementPlacement placement
    ) {
        return advertisementFeedService.getPromotedAdvertisement(
                placement,
                getCurrentUser()
        );
    }

    /**
     * Returns multiple promoted advertisements
     * for the specified placement.
     */
    @GetMapping("/feed/{placement}/{limit}")
    public List<AdvertisementResponse> getPromotedAdvertisements(
            @PathVariable AdvertisementPlacement placement,
            @PathVariable Integer limit
    ) {
        return advertisementFeedService.getPromotedAdvertisements(
                placement,
                getCurrentUser(),
                limit
        );
    }

    /**
     * Records a click whenever a user
     * clicks on an advertisement.
     */
    @PostMapping("/{advertisementId}/click")
    @ResponseStatus(HttpStatus.OK)
    public void recordClick(
            @PathVariable UUID advertisementId
    ) {
        advertisementService.recordClick(
                advertisementId
        );
    }

    /**
     * Records an advertisement impression
     * whenever it is displayed to a user.
     */
    @PostMapping("/{advertisementId}/impression")
    @ResponseStatus(HttpStatus.OK)
    public void recordImpression(
            @PathVariable UUID advertisementId
    ) {
        advertisementService.recordImpression(
                advertisementId
        );
    }

    /**
     * Retrieves performance statistics
     * for a single advertisement.
     */
    @GetMapping("/statistics/{advertisementId}")
    public AdvertisementStatisticsResponse getAdvertisementStatistics(
            @PathVariable UUID advertisementId
    ) {
        return advertisementService.getAdvertisementStatistics(
                advertisementId
        );
    }

    /**
     * Retrieves the advertiser dashboard,
     * including campaign summaries and analytics.
     */
    @GetMapping("/dashboard")
    public AdvertisementDashboardResponse getDashboard() {
        return advertisementService.getDashboard();
    }

    /**
     * Retrieves advertisements awaiting approval.
     * Intended for administrators.
     */
    @GetMapping("/admin/pending")
    public List<AdvertisementResponse> getPendingAdvertisements() {
        return advertisementService.getPendingAdvertisements();
    }

    /**
     * Retrieves all currently active advertisements.
     * Intended for administrators.
     */
    @GetMapping("/admin/active")
    public List<AdvertisementResponse> getActiveAdvertisements() {
        return advertisementService.getActiveAdvertisements();
    }
}