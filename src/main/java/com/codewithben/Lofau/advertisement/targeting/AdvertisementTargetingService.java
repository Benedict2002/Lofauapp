package com.codewithben.Lofau.advertisement.targeting;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.advertisement.entity.Advertisement;

public interface AdvertisementTargetingService {

    boolean canView(
            Advertisement advertisement,
            User user
    );

}