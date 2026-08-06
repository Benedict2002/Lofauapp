package com.codewithben.Lofau.admin.advertisement.service;

import com.codewithben.Lofau.advertisement.dto.response.AdvertisementDashboardResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementStatisticsResponse;

import java.util.List;
import java.util.UUID;

public interface AdminAdvertisementService {

    /*
     * =====================================
     * Campaign Moderation
     * =====================================
     */

    AdvertisementResponse approveAdvertisement(UUID advertisementId);

    AdvertisementResponse rejectAdvertisement(UUID advertisementId);

    AdvertisementResponse activateAdvertisement(UUID advertisementId);

    AdvertisementResponse deactivateAdvertisement(UUID advertisementId);

    AdvertisementResponse deleteAdvertisement(UUID advertisementId);

    /*
     * =====================================
     * Campaign Management
     * =====================================
     */

    List<AdvertisementResponse> getPendingAdvertisements();

    List<AdvertisementResponse> getActiveAdvertisements();

    List<AdvertisementResponse> getAllAdvertisements();

    /*
     * =====================================
     * Analytics
     * =====================================
     */

    AdvertisementStatisticsResponse getAdvertisementStatistics(
            UUID advertisementId
    );

    AdvertisementDashboardResponse getDashboard();
}