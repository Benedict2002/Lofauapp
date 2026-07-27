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
import com.codewithben.Lofau.advertisement.analytics.AdvertisementAnalyticsService;
import com.codewithben.Lofau.advertisement.placement.AdvertisementPlacementService;
import com.codewithben.Lofau.advertisement.targeting.AdvertisementTargetingService;
import com.codewithben.Lofau.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    ) {

        User advertiser = getCurrentUser();

        Advertisement advertisement =
                advertisementMapper.toEntity(request);

        advertisement.setAdvertiser(advertiser);

        advertisementValidator.validateForCreation(advertisement);

        Advertisement savedAdvertisement =
                advertisementRepository.save(advertisement);

        /*
         * Upload media if provided
         * (We'll implement this once Media is integrated)
         */

        return advertisementMapper.toResponse(savedAdvertisement);
    }

    @Override
    @Transactional
    public AdvertisementResponse updateAdvertisement(
            UUID advertisementId,
            UpdateAdvertisementRequest request
    ) {

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
    public AdvertisementResponse approveAdvertisement(
            UUID advertisementId
    ) {

        Advertisement advertisement =
                findAdvertisement(advertisementId);

        advertisementValidator.validateForApproval(
                advertisement
        );

        advertisement.setApproved(true);

        advertisementRepository.save(
                advertisement
        );

        return advertisementMapper.toResponse(
                advertisement
        );
    }

    @Override
    @Transactional
    public AdvertisementResponse rejectAdvertisement(
            UUID advertisementId
    ) {

        Advertisement advertisement =
                findAdvertisement(advertisementId);

        advertisement.setApproved(false);

        advertisement.setStatus(
                AdvertisementStatus.REJECTED
        );

        advertisementRepository.save(
                advertisement
        );

        return advertisementMapper.toResponse(
                advertisement
        );
    }

    @Override
    @Transactional
    public AdvertisementResponse activateAdvertisement(
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

        advertisementRepository.save(
                advertisement
        );

        return advertisementMapper.toResponse(
                advertisement
        );
    }

    @Override
    @Transactional
    public AdvertisementResponse deactivateAdvertisement(
            UUID advertisementId
    ) {

        Advertisement advertisement =
                findAdvertisement(advertisementId);

        advertisement.setActive(false);

        advertisement.setStatus(
                AdvertisementStatus.INACTIVE
        );

        advertisementRepository.save(
                advertisement
        );

        return advertisementMapper.toResponse(
                advertisement
        );
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
    public AdvertisementStatisticsResponse getAdvertisementStatistics(
            UUID advertisementId
    ) {

        Advertisement advertisement =
                findAdvertisement(advertisementId);

        double ctr = 0;

        if (advertisement.getImpressions() > 0) {

            ctr = (double) advertisement.getClicks()
                    / advertisement.getImpressions() * 100;
        }

        return AdvertisementStatisticsResponse.builder()

                .impressions(advertisement.getImpressions())

                .clicks(advertisement.getClicks())

                .ctr(ctr)

                .totalBudget(advertisement.getTotalBudget())

                .spentBudget(advertisement.getSpentBudget())

                .remainingBudget(
                        advertisement.getRemainingBudget()
                )

                .build();
    }

    @Override
    public AdvertisementDashboardResponse getDashboard() {

        User advertiser = getCurrentUser();

        List<Advertisement> advertisements =
                advertisementRepository.findByAdvertiser(advertiser);

        int impressions = advertisements.stream()

                .mapToInt(Advertisement::getImpressions)

                .sum();

        int clicks = advertisements.stream()

                .mapToInt(Advertisement::getClicks)

                .sum();

        int spent = advertisements.stream()

                .mapToInt(Advertisement::getSpentBudget)

                .sum();

        long active = advertisements.stream()

                .filter(Advertisement::getActive)

                .count();

        long pending = advertisements.stream()

                .filter(ad -> !ad.getApproved())

                .count();

        double ctr = impressions == 0

                ? 0

                : (double) clicks / impressions * 100;

        return AdvertisementDashboardResponse.builder()

                .totalAdvertisements(advertisements.size())

                .activeAdvertisements((int) active)

                .pendingAdvertisements((int) pending)

                .totalImpressions(impressions)

                .totalClicks(clicks)

                .totalSpent(spent)

                .averageCTR(ctr)

                .build();
    }

    @Override
    public List<AdvertisementResponse> getPendingAdvertisements() {

        return advertisementRepository

                .findByApprovedFalseAndDeletedFalse()

                .stream()

                .map(advertisementMapper::toResponse)

                .toList();
    }

    @Override
    public List<AdvertisementResponse> getActiveAdvertisements() {

        return advertisementRepository

                .findByApprovedTrueAndActiveTrueAndDeletedFalse()

                .stream()

                .map(advertisementMapper::toResponse)

                .toList();
    }
}