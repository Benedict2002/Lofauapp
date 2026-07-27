package com.codewithben.Lofau.advertisement.engine;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.advertisement.analytics.AdvertisementAnalyticsService;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;
import com.codewithben.Lofau.advertisement.mapper.AdvertisementMapper;
import com.codewithben.Lofau.advertisement.placement.AdvertisementPlacementService;
import com.codewithben.Lofau.advertisement.repository.AdvertisementRepository;
import com.codewithben.Lofau.advertisement.targeting.AdvertisementTargetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Responsible for selecting and serving advertisements.
 *
 * Flow:
 * 1. Load advertisements for a placement.
 * 2. Filter advertisements using targeting rules.
 * 3. Rank advertisements using the placement scoring algorithm.
 * 4. Record an impression.
 * 5. Return advertisement response(s).
 */
@Service
@RequiredArgsConstructor
public class AdvertisementEngineImpl
        implements AdvertisementEngine {

    private final AdvertisementRepository advertisementRepository;

    private final AdvertisementPlacementService placementService;

    private final AdvertisementTargetingService targetingService;

    private final AdvertisementAnalyticsService analyticsService;

    private final AdvertisementMapper advertisementMapper;

    /**
     * Returns the highest-ranked advertisement for the given placement.
     */
    @Override
    public AdvertisementResponse serveAdvertisement(
            AdvertisementPlacement placement,
            User user
    ) {

        // Load advertisements that are active, approved,
        // belong to the requested placement and are not deleted.
        List<Advertisement> advertisements =
                advertisementRepository
                        .findByApprovedTrueAndActiveTrueAndPlacementAndDeletedFalse(
                                placement
                        );

        // Filter advertisements the user is allowed to see
        // then choose the advertisement with the highest score.
        Advertisement advertisement =
                advertisements.stream()

                        .filter(ad ->
                                targetingService.canView(ad, user)
                        )

                        .max(
                                Comparator.comparingDouble(
                                        placementService::calculateScore
                                )
                        )

                        .orElse(null);

        // No advertisement available.
        if (advertisement == null) {
            return null;
        }

        // Record impression for analytics.
        analyticsService.recordImpression(
                advertisement.getId()
        );

        // Convert entity into API response.
        return advertisementMapper.toResponse(
                advertisement
        );
    }

    /**
     * Returns multiple advertisements ordered by ranking score.
     */
    @Override
    public List<AdvertisementResponse> serveAdvertisements(
            AdvertisementPlacement placement,
            User user,
            int limit
    ) {

        // Load all advertisements for the requested placement.
        List<Advertisement> advertisements =
                advertisementRepository
                        .findByApprovedTrueAndActiveTrueAndPlacementAndDeletedFalse(
                                placement
                        );

        return advertisements.stream()

                // Apply targeting rules.
                .filter(ad ->
                        targetingService.canView(ad, user)
                )

                // Highest score appears first.
                .sorted(
                        (a, b) ->
                                Double.compare(
                                        placementService.calculateScore(b),
                                        placementService.calculateScore(a)
                                )
                )

                // Return only the requested number of advertisements.
                .limit(limit)

                // Record an impression for every served advertisement.
                .peek(ad ->
                        analyticsService.recordImpression(
                                ad.getId()
                        )
                )

                // Convert entities into DTOs.
                .map(advertisementMapper::toResponse)

                .toList();
    }

}