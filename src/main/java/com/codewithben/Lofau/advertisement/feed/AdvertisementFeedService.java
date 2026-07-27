package com.codewithben.Lofau.advertisement.feed;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;

import java.util.List;

public interface AdvertisementFeedService {

    AdvertisementResponse getPromotedAdvertisement(
            AdvertisementPlacement placement,
            User user
    );

    List<AdvertisementResponse> getPromotedAdvertisements(
            AdvertisementPlacement placement,
            User user,
            int limit
    );

}