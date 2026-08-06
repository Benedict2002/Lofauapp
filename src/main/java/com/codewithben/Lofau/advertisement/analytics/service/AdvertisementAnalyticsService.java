package com.codewithben.Lofau.advertisement.analytics.service;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.advertisement.analytics.dto.response.AdvertisementAnalyticsResponse;
import com.codewithben.Lofau.advertisement.analytics.dto.response.AdvertisementPerformanceResponse;

import java.util.UUID;

public interface AdvertisementAnalyticsService {

    AdvertisementAnalyticsResponse getAnalytics(UUID advertisementId);

    AdvertisementPerformanceResponse getPlatformAnalytics();

    void recordImpression(UUID advertisementId);

    void recordClick(UUID advertisementId);

    void recordSave(UUID advertisementId);

    void recordShare(UUID advertisementId);

    void recordConversion(UUID advertisementId);
    AdvertisementPerformanceResponse getAdvertiserAnalytics(User advertiser);

}