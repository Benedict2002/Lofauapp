package com.codewithben.Lofau.advertisement.placement;

import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.media.enums.OwnerType;

import java.util.List;
import java.util.UUID;

public interface AdvertisementPlacementService {

    List<AdvertisementResponse> getFeedAdvertisements(
            UUID userId,
            OwnerType ownerType
    );

    List<AdvertisementResponse> getSidebarAdvertisements(
            UUID userId
    );

    List<AdvertisementResponse> getSponsoredCarousel(
            UUID userId
    );

    AdvertisementResponse getSponsoredBanner(
            UUID userId
    );

    double calculateScore(Advertisement b);
}