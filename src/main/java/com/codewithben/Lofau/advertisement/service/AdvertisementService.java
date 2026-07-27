package com.codewithben.Lofau.advertisement.service;

import com.codewithben.Lofau.advertisement.dto.request.CreateAdvertisementRequest;
import com.codewithben.Lofau.advertisement.dto.request.UpdateAdvertisementRequest;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementDashboardResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementStatisticsResponse;

import java.util.List;
import java.util.UUID;

public interface AdvertisementService {

    AdvertisementResponse createAdvertisement(
            CreateAdvertisementRequest request
    );

    AdvertisementResponse updateAdvertisement(
            UUID advertisementId,
            UpdateAdvertisementRequest request
    );

    AdvertisementResponse getAdvertisement(
            UUID advertisementId
    );

    List<AdvertisementResponse> getAdvertisements();

    List<AdvertisementResponse> getMyAdvertisements();

    List<AdvertisementResponse> getAdvertisementsByPlacement(
            String placement
    );

    AdvertisementResponse approveAdvertisement(
            UUID advertisementId
    );

    AdvertisementResponse rejectAdvertisement(
            UUID advertisementId
    );

    AdvertisementResponse activateAdvertisement(
            UUID advertisementId
    );

    AdvertisementResponse deactivateAdvertisement(
            UUID advertisementId
    );

    AdvertisementResponse pauseAdvertisement(
            UUID advertisementId
    );

    AdvertisementResponse resumeAdvertisement(
            UUID advertisementId
    );

    void deleteAdvertisement(
            UUID advertisementId
    );
    void recordClick(UUID advertisementId);

    void recordImpression(UUID advertisementId);

    AdvertisementStatisticsResponse getAdvertisementStatistics(
            UUID advertisementId
    );

    AdvertisementDashboardResponse getDashboard();

    List<AdvertisementResponse> getPendingAdvertisements();

    List<AdvertisementResponse> getActiveAdvertisements();

}