package com.codewithben.Lofau.advertisement.service.impl;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.advertisement.dto.request.CreateAdvertisementRequest;
import com.codewithben.Lofau.advertisement.dto.request.UpdateAdvertisementRequest;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementDashboardResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementStatisticsResponse;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import com.codewithben.Lofau.advertisement.mapper.AdvertisementMapper;
import com.codewithben.Lofau.advertisement.repository.AdvertisementRepository;
import com.codewithben.Lofau.advertisement.service.AdvertisementService;
import com.codewithben.Lofau.advertisement.validator.AdvertisementValidator;
import com.codewithben.Lofau.advertisement.analytics.service.AdvertisementAnalyticsService;
import com.codewithben.Lofau.advertisement.placement.AdvertisementPlacementService;
import com.codewithben.Lofau.advertisement.targeting.AdvertisementTargetingService;
import com.codewithben.Lofau.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl
        implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementMapper advertisementMapper;
    private final AdvertisementValidator advertisementValidator;
    private final AdvertisementAnalyticsService analyticsService;
    private final AdvertisementPlacementService placementService;
    private final AdvertisementTargetingService targetingService;
    private final MediaService mediaService;
    private final UserRepository userRepository;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated.");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found."));
    }

    private Advertisement findAdvertisement(UUID id) {

        return advertisementRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Advertisement not found."));
    }

    @Override
    @Transactional
    public AdvertisementResponse createAdvertisement(
            CreateAdvertisementRequest request
    ) throws IOException {

        // Get the currently authenticated user
        User advertiser = getCurrentUser();

        // Convert request into Advertisement entity
        Advertisement advertisement =
                advertisementMapper.toEntity(request);

        // Set the owner
        advertisement.setAdvertiser(advertiser);

        // Validate advertisement before saving
        advertisementValidator.validateForCreation(advertisement);

        // Save advertisement first to generate its UUID
        Advertisement savedAdvertisement =
                advertisementRepository.save(advertisement);

        /*
         * Upload advertisement media if provided.
         * One advertisement supports one media file
         * (image, gif or video).
         */
        if (request.getMedia() != null && !request.getMedia().isEmpty()) {
            mediaService.uploadAdvertisement(
                    savedAdvertisement.getId(),
                    request.getMedia()
            );
        }

        //Return advertisement response
        return advertisementMapper.toResponse(savedAdvertisement);
    }

    @Override
    @Transactional
    public AdvertisementResponse updateAdvertisement(
            UUID advertisementId,
            UpdateAdvertisementRequest request
    ) throws IOException {

        Advertisement advertisement =
                findAdvertisement(advertisementId);

        User currentUser = getCurrentUser();

        if (!advertisement.getAdvertiser().getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "You are not allowed to update this advertisement."
            );
        }

        if (advertisement.getApproved()) {

            throw new RuntimeException(
                    "Approved advertisements cannot be edited."
            );
        }

        advertisementMapper.updateEntity(
                advertisement,
                request
        );

        advertisementValidator.validateForCreation(
                advertisement
        );

        Advertisement updatedAdvertisement =
                advertisementRepository.save(
                        advertisement
                );
        if (request.getMedia() != null && !request.getMedia().isEmpty()) {
            mediaService.uploadAdvertisement(
                    advertisement.getId(),
                    request.getMedia()
            );
        }

        return advertisementMapper.toResponse(
                updatedAdvertisement
        );

    }

    @Override
    @Transactional(readOnly = true)
    public AdvertisementResponse getAdvertisement(
            UUID advertisementId
    ) {

        Advertisement advertisement =
                findAdvertisement(advertisementId);

        analyticsService.recordImpression(advertisementId);

        return advertisementMapper.toResponse(advertisement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementResponse> getAdvertisements() {

        return advertisementRepository.findAll()
                .stream()
                .filter(advertisement -> !advertisement.getDeleted())
                .map(advertisementMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementResponse> getMyAdvertisements() {

        User currentUser = getCurrentUser();

        return advertisementRepository
                .findByAdvertiserAndDeletedFalse(currentUser)
                .stream()
                .map(advertisementMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementResponse> getAdvertisementsByPlacement(
            String placement
    ) {

        return advertisementRepository
                .findByPlacementAndDeletedFalse(
                        AdvertisementPlacement.valueOf(placement.toUpperCase())
                )
                .stream()
                .map(advertisementMapper::toResponse)
                .toList();
    }



    @Override
    @Transactional
    public AdvertisementResponse pauseAdvertisement(
            UUID advertisementId
    ) {

        Advertisement advertisement =
                findAdvertisement(advertisementId);

        if (!advertisement.getApproved()) {
            throw new RuntimeException(
                    "Advertisement has not been approved."
            );
        }

        if (!advertisement.getActive()) {
            throw new RuntimeException(
                    "Advertisement is already inactive."
            );
        }

        advertisement.setStatus(
                AdvertisementStatus.PAUSED
        );

        advertisement.setActive(false);

        Advertisement saved =
                advertisementRepository.save(advertisement);

        return advertisementMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public AdvertisementResponse resumeAdvertisement(
            UUID advertisementId
    ) {

        Advertisement advertisement =
                findAdvertisement(advertisementId);

        advertisementValidator.validateForActivation(
                advertisement
        );

        advertisement.setStatus(
                AdvertisementStatus.ACTIVE
        );

        advertisement.setActive(true);

        Advertisement saved =
                advertisementRepository.save(advertisement);

        return advertisementMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteAdvertisement(
            UUID advertisementId
    ) {

        Advertisement advertisement =
                findAdvertisement(advertisementId);

        User currentUser = getCurrentUser();

        if (!advertisement.getAdvertiser().getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "You are not allowed to delete this advertisement."
            );
        }

        advertisement.setDeleted(true);

        advertisement.setActive(false);

        advertisement.setStatus(
                AdvertisementStatus.DELETED
        );

        advertisementRepository.save(advertisement);
    }

    @Override
    @Transactional
    public void recordClick(UUID advertisementId) {

        analyticsService.recordClick(advertisementId);
    }

    @Override
    @Transactional
    public void recordImpression(UUID advertisementId) {

        analyticsService.recordImpression(advertisementId);
    }

    @Override
    @Transactional(readOnly = true)
    public AdvertisementStatisticsResponse getAdvertisementStatistics(
            UUID advertisementId
    ) {

        return advertisementMapper.toStatisticsResponse(
                analyticsService.getAnalytics(advertisementId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AdvertisementDashboardResponse getDashboard() {

        return advertisementMapper.toDashboardResponse(

                analyticsService.getAdvertiserAnalytics(
                        getCurrentUser()
                )

        );
    }
}