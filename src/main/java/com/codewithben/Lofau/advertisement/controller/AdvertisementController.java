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
     * Creates a new advertisement.
     * Only authenticated users can create advertisements.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertisementResponse createAdvertisement(
            @Valid @RequestBody CreateAdvertisementRequest request
    ) {
        return advertisementService.createAdvertisement(request);
    }

    /**
     * Updates an existing advertisement.
     * Only the advertisement owner can update it.
     */
    @PutMapping("/{advertisementId}")
    public AdvertisementResponse updateAdvertisement(
            @PathVariable UUID advertisementId,
            @Valid @RequestBody UpdateAdvertisementRequest request
    ) {
        return advertisementService.updateAdvertisement(
                advertisementId,
                request
        );
    }

    @GetMapping("/{advertisementId}")
    public AdvertisementResponse getAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        return advertisementService.getAdvertisement(
                advertisementId
        );
    }

    @GetMapping
    public List<AdvertisementResponse> getAdvertisements() {

        return advertisementService.getAdvertisements();
    }

    @GetMapping("/mine")
    public List<AdvertisementResponse> getMyAdvertisements() {

        return advertisementService.getMyAdvertisements();
    }

    @GetMapping("/placement/{placement}")
    public List<AdvertisementResponse> getByPlacement(
            @PathVariable AdvertisementPlacement placement
    ) {

        return advertisementService.getAdvertisementsByPlacement(
                placement.name()
        );
    }

    @PatchMapping("/{advertisementId}/approve")
    public AdvertisementResponse approveAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        return advertisementService.approveAdvertisement(
                advertisementId
        );
    }

    @PatchMapping("/{advertisementId}/reject")
    public AdvertisementResponse rejectAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        return advertisementService.rejectAdvertisement(
                advertisementId
        );
    }

    @PatchMapping("/{advertisementId}/activate")
    public AdvertisementResponse activateAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        return advertisementService.activateAdvertisement(
                advertisementId
        );
    }

    @PatchMapping("/{advertisementId}/deactivate")
    public AdvertisementResponse deactivateAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        return advertisementService.deactivateAdvertisement(
                advertisementId
        );
    }

    @PatchMapping("/{advertisementId}/pause")
    public AdvertisementResponse pauseAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        return advertisementService.pauseAdvertisement(
                advertisementId
        );
    }

    @PatchMapping("/{advertisementId}/resume")
    public AdvertisementResponse resumeAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        return advertisementService.resumeAdvertisement(
                advertisementId
        );
    }

    @DeleteMapping("/{advertisementId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdvertisement(
            @PathVariable UUID advertisementId
    ) {

        advertisementService.deleteAdvertisement(
                advertisementId
        );
    }

    @GetMapping("/feed/{placement}")
    public AdvertisementResponse getPromotedAdvertisement(
            @PathVariable AdvertisementPlacement placement
    ) {

        return advertisementFeedService.getPromotedAdvertisement(
                placement,
                getCurrentUser()
        );
    }

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

    @PostMapping("/{advertisementId}/click")
    @ResponseStatus(HttpStatus.OK)
    public void recordClick(
            @PathVariable UUID advertisementId
    ) {

        advertisementService.recordClick(
                advertisementId
        );
    }

    @PostMapping("/{advertisementId}/impression")
    @ResponseStatus(HttpStatus.OK)
    public void recordImpression(
            @PathVariable UUID advertisementId
    ) {

        advertisementService.recordImpression(
                advertisementId
        );
    }

    @GetMapping("/statistics/{advertisementId}")
    public AdvertisementStatisticsResponse getAdvertisementStatistics(
            @PathVariable UUID advertisementId
    ) {

        return advertisementService.getAdvertisementStatistics(
                advertisementId
        );
    }

    @GetMapping("/dashboard")
    public AdvertisementDashboardResponse getDashboard() {

        return advertisementService.getDashboard();
    }

    @GetMapping("/admin/pending")
    public List<AdvertisementResponse> getPendingAdvertisements() {

        return advertisementService.getPendingAdvertisements();
    }

    @GetMapping("/admin/active")
    public List<AdvertisementResponse> getActiveAdvertisements() {

        return advertisementService.getActiveAdvertisements();
    }
}