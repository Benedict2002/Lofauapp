package com.codewithben.Lofau.advertisement.analytics;

import com.codewithben.Lofau.advertisement.analytics.dto.response.AdvertisementAnalyticsResponse;
import com.codewithben.Lofau.advertisement.analytics.dto.response.AdvertisementPerformanceResponse;
import com.codewithben.Lofau.advertisement.analytics.mapper.AdvertisementAnalyticsMapper;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.advertisement.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdvertisementAnalyticsServiceImpl
        implements AdvertisementAnalyticsService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementAnalyticsMapper mapper;

    private Advertisement getAdvertisement(UUID id) {

        return advertisementRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Advertisement not found"));
    }

    @Override
    public AdvertisementAnalyticsResponse getAnalytics(UUID advertisementId) {

        return mapper.toResponse(getAdvertisement(advertisementId));
    }

    @Override
    public AdvertisementPerformanceResponse getPlatformAnalytics() {

        int totalAds = (int) advertisementRepository.count();

        int impressions = advertisementRepository.findAll()
                .stream()
                .mapToInt(Advertisement::getImpressions)
                .sum();

        int clicks = advertisementRepository.findAll()
                .stream()
                .mapToInt(Advertisement::getClicks)
                .sum();

        int conversions = advertisementRepository.findAll()
                .stream()
                .mapToInt(Advertisement::getConversions)
                .sum();

        int spent = advertisementRepository.findAll()
                .stream()
                .mapToInt(Advertisement::getSpentBudget)
                .sum();

        int active = (int) advertisementRepository.findAll()
                .stream()
                .filter(Advertisement::getActive)
                .count();

        double ctr = impressions == 0
                ? 0
                : (double) clicks / impressions;

        return AdvertisementPerformanceResponse.builder()
                .totalAdvertisements(totalAds)
                .activeAdvertisements(active)
                .totalImpressions(impressions)
                .totalClicks(clicks)
                .totalConversions(conversions)
                .overallCTR(ctr)
                .totalRevenueSpent(spent)
                .build();
    }

    @Override
    public void recordImpression(UUID advertisementId) {

        Advertisement ad = getAdvertisement(advertisementId);

        ad.setImpressions(ad.getImpressions() + 1);

        advertisementRepository.save(ad);
    }

    @Override
    public void recordClick(UUID advertisementId) {

        Advertisement ad = getAdvertisement(advertisementId);

        ad.setClicks(ad.getClicks() + 1);

        advertisementRepository.save(ad);
    }

    @Override
    public void recordSave(UUID advertisementId) {

        Advertisement ad = getAdvertisement(advertisementId);

        ad.setSaves(ad.getSaves() + 1);

        advertisementRepository.save(ad);
    }

    @Override
    public void recordShare(UUID advertisementId) {

        Advertisement ad = getAdvertisement(advertisementId);

        ad.setShares(ad.getShares() + 1);

        advertisementRepository.save(ad);
    }

    @Override
    public void recordConversion(UUID advertisementId) {

        Advertisement ad = getAdvertisement(advertisementId);

        ad.setConversions(ad.getConversions() + 1);

        advertisementRepository.save(ad);
    }

}