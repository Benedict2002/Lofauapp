package com.codewithben.Lofau.advertisement.engine;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;

import java.util.List;

public interface AdvertisementEngine {

    AdvertisementResponse serveAdvertisement(
            AdvertisementPlacement placement,
            User user
    );

    List<AdvertisementResponse> serveAdvertisements(
            AdvertisementPlacement placement,
            User user,
            int limit
    );

}