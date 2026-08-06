package com.codewithben.Lofau.admin.advertisement.service.impl;

import com.codewithben.Lofau.admin.advertisement.service.AdminAdvertisementService;
import com.codewithben.Lofau.admin.audit.enums.AuditAction;
import com.codewithben.Lofau.admin.audit.enums.AuditEntityType;
import com.codewithben.Lofau.admin.audit.service.AuditService;
import com.codewithben.Lofau.advertisement.analytics.service.AdvertisementAnalyticsService;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementDashboardResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementStatisticsResponse;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import com.codewithben.Lofau.advertisement.mapper.AdvertisementMapper;
import com.codewithben.Lofau.advertisement.repository.AdvertisementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAdvertisementServiceImpl
        implements AdminAdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    private final AdvertisementMapper advertisementMapper;

    private final AuditService auditService;
    private final AdvertisementAnalyticsService advertisementAnalyticsService;

    /*
     * ============================================
     * Campaign Moderation
     * ============================================
     */

    @Override
    public AdvertisementResponse approveAdvertisement(UUID advertisementId) {

        Advertisement advertisement = getAdvertisement(advertisementId);

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

        return advertisementMapper.toResponse(advertisement);
    }

    @Override
    public AdvertisementResponse rejectAdvertisement(UUID advertisementId) {

        Advertisement advertisement = getAdvertisement(advertisementId);

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

        return advertisementMapper.toResponse(advertisement);
    }

    @Override
    public AdvertisementResponse activateAdvertisement(UUID advertisementId) {

        Advertisement advertisement = getAdvertisement(advertisementId);

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

        return advertisementMapper.toResponse(advertisement);
    }

    @Override
    public AdvertisementResponse deactivateAdvertisement(UUID advertisementId) {

        Advertisement advertisement = getAdvertisement(advertisementId);

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

        return advertisementMapper.toResponse(advertisement);
    }

    @Override
    public AdvertisementResponse deleteAdvertisement(UUID advertisementId) {

        Advertisement advertisement = getAdvertisement(advertisementId);

        advertisement.setDeleted(true);
        advertisement.setActive(false);
        advertisement.setStatus(AdvertisementStatus.DELETED);

        advertisementRepository.save(advertisement);

        auditService.log(
                AuditAction.ADVERTISEMENT_DELETED,
                AuditEntityType.ADVERTISEMENT,
                advertisement.getId(),
                advertisement.getTitle(),
                "Advertisement deleted.",
                null
        );

        return advertisementMapper.toResponse(advertisement);
    }

    /*
     * ============================================
     * Campaign Queries
     * ============================================
     */

    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementResponse> getPendingAdvertisements() {

        return advertisementRepository
                .findByStatus(AdvertisementStatus.PENDING_APPROVAL)
                .stream()
                .map(advertisementMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementResponse> getActiveAdvertisements() {

        return advertisementRepository
                .findByStatus(AdvertisementStatus.ACTIVE)
                .stream()
                .map(advertisementMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementResponse> getAllAdvertisements() {

        return advertisementRepository
                .findAll()
                .stream()
                .map(advertisementMapper::toResponse)
                .toList();
    }

    /*
     * ============================================
     * Analytics
     * ============================================
     */

    @Override
    @Transactional(readOnly = true)
    public AdvertisementStatisticsResponse getAdvertisementStatistics(
            UUID advertisementId
    ) {

        var analytics =
                advertisementAnalyticsService.getAnalytics(advertisementId);

        Advertisement advertisement = getAdvertisement(advertisementId);

        return AdvertisementStatisticsResponse.builder()

                .impressions(analytics.getImpressions())

                .clicks(analytics.getClicks())

                .ctr(analytics.getCtr())

                .totalBudget(advertisement.getTotalBudget())

                .spentBudget(analytics.getSpentBudget())

                .remainingBudget(analytics.getRemainingBudget())

                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AdvertisementDashboardResponse getDashboard() {

        return advertisementMapper.toDashboardResponse(

                advertisementAnalyticsService.getPlatformAnalytics()

        );
    }

    /*
     * ============================================
     * Helpers
     * ============================================
     */

    private Advertisement getAdvertisement(UUID advertisementId) {

        return advertisementRepository.findById(advertisementId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Advertisement not found."
                        ));
    }
}