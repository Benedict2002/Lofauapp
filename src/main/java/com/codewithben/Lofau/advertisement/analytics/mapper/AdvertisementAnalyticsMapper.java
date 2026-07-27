package com.codewithben.Lofau.advertisement.analytics.mapper;




import com.codewithben.Lofau.advertisement.analytics.dto.response.AdvertisementAnalyticsResponse;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import org.springframework.stereotype.Component;

@Component
public class AdvertisementAnalyticsMapper {

    public AdvertisementAnalyticsResponse toResponse(
            Advertisement advertisement
    ) {

        return AdvertisementAnalyticsResponse.builder()
                .impressions(advertisement.getImpressions())
                .clicks(advertisement.getClicks())
                .shares(advertisement.getShares())
                .saves(advertisement.getSaves())
                .conversions(advertisement.getConversions())
                .ctr(advertisement.getCtr())
                .conversionRate(advertisement.getConversionRate())
                .spentBudget(advertisement.getSpentBudget())
                .remainingBudget(advertisement.getRemainingBudget())
                .build();
    }

}
