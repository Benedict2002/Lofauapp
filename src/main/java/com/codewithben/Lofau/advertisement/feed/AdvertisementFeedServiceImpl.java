package com.codewithben.Lofau.advertisement.feed;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.engine.AdvertisementEngine;
import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Responsible for providing advertisements to
 * different LOFAU feeds.
 *
 * The feed service delegates advertisement
 * selection to the Advertisement Engine.
 */
@Service
@RequiredArgsConstructor
public class AdvertisementFeedServiceImpl
        implements AdvertisementFeedService {

    private final AdvertisementEngine advertisementEngine;

    /**
     * Returns one promoted advertisement.
     */
    @Override
    public AdvertisementResponse getPromotedAdvertisement(
            AdvertisementPlacement placement,
            User user
    ) {

        return advertisementEngine.serveAdvertisement(
                placement,
                user
        );
    }

    /**
     * Returns multiple promoted advertisements.
     */
    @Override
    public List<AdvertisementResponse> getPromotedAdvertisements(
            AdvertisementPlacement placement,
            User user,
            int limit
    ) {

        return advertisementEngine.serveAdvertisements(
                placement,
                user,
                limit
        );
    }

}