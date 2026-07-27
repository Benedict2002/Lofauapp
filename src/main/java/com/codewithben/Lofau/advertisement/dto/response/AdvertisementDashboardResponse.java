package com.codewithben.Lofau.advertisement.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdvertisementDashboardResponse {

    private Integer totalAdvertisements;

    private Integer activeAdvertisements;

    private Integer pendingAdvertisements;

    private Integer totalImpressions;

    private Integer totalClicks;

    private Integer totalSpent;

    private Double averageCTR;

}