package com.codewithben.Lofau.advertisement.analytics.service;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.advertisement.analytics.dto.response.AdvertisementAnalyticsResponse;
import com.codewithben.Lofau.advertisement.analytics.dto.response.AdvertisementPerformanceResponse;
import com.codewithben.Lofau.advertisement.analytics.mapper.AdvertisementAnalyticsMapper;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.advertisement.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

        Long totalAds = advertisementRepository.count();
        List<Advertisement> advertisements = advertisementRepository.findAll();

        Long impressions = advertisements
                .stream()
                .mapToLong(Advertisement::getImpressions)
                .sum();

        Long clicks = advertisements
                .stream()
                .mapToLong(Advertisement::getClicks)
                .sum();

        Long conversions = advertisements
                .stream()
                .mapToLong(Advertisement::getConversions)
                .sum();

        Long spent = advertisements
                .stream()
                .mapToLong(Advertisement::getSpentBudget)
                .sum();

        Long active = advertisements
                .stream()
                .filter(Advertisement::getActive)
                .count();

        double ctr = impressions == 0
                ? 0.0
                : ((double) clicks / impressions) * 100;

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

        Advertisement advertisement = getAdvertisement(advertisementId);

        advertisement.setImpressions(
                advertisement.getImpressions() + 1
        );

        advertisementRepository.save(advertisement);
    }

    @Override
    public void recordClick(UUID advertisementId) {

        Advertisement advertisement = getAdvertisement(advertisementId);

        advertisement.setClicks(
                advertisement.getClicks() + 1
        );

        advertisementRepository.save(advertisement);
    }

    @Override
    public void recordSave(UUID advertisementId) {

        Advertisement advertisement = getAdvertisement(advertisementId);

        advertisement.setSaves(
                advertisement.getSaves() + 1
        );

        advertisementRepository.save(advertisement);
    }

    @Override
    public void recordShare(UUID advertisementId) {

        Advertisement advertisement = getAdvertisement(advertisementId);

        advertisement.setShares(
                advertisement.getShares() + 1
        );

        advertisementRepository.save(advertisement);
    }

    @Override
    public void recordConversion(UUID advertisementId) {

        Advertisement advertisement = getAdvertisement(advertisementId);

        advertisement.setConversions(
                advertisement.getConversions() + 1
        );

        advertisementRepository.save(advertisement);
    }

    @Override
    public AdvertisementPerformanceResponse getAdvertiserAnalytics(
            User advertiser
    ) {

        List<Advertisement> advertisements =
                advertisementRepository.findByAdvertiser(advertiser);

        Long impressions = advertisements.stream()
                .mapToLong(Advertisement::getImpressions)
                .sum();

        Long clicks = advertisements.stream()
                .mapToLong(Advertisement::getClicks)
                .sum();

        Long conversions = advertisements.stream()
                .mapToLong(Advertisement::getConversions)
                .sum();

        Long spent = advertisements.stream()
                .mapToLong(Advertisement::getSpentBudget)
                .sum();

        Long active = advertisements.stream()
                .filter(Advertisement::getActive)
                .count();

        double ctr = impressions == 0
                ? 0.0
                : ((double) clicks / impressions) * 100;

        return AdvertisementPerformanceResponse.builder()

                .totalAdvertisements((long) advertisements.size())

                .activeAdvertisements(active)

                .totalImpressions(impressions)

                .totalClicks(clicks)

                .totalConversions(conversions)

                .overallCTR(ctr)

                .totalRevenueSpent(spent)

                .build();
    }
}