package com.codewithben.Lofau.advertisement.placement;

import com.codewithben.Lofau.advertisement.entity.Advertisement;

public interface AdvertisementPlacementStrategy {

    double calculateScore(Advertisement advertisement);
}