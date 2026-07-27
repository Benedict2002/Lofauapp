package com.codewithben.Lofau.advertisement.targeting;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdvertisementTargetingServiceImpl
        implements AdvertisementTargetingService {

    @Override
    public boolean canView(
            Advertisement advertisement,
            User user
    ) {

        if (!advertisement.getApproved()) {
            return false;
        }

        if (!advertisement.getActive()) {
            return false;
        }

        if (advertisement.getDeleted()) {
            return false;
        }

        if (advertisement.getStatus() != AdvertisementStatus.ACTIVE) {
            return false;
        }

        if (advertisement.getPaused()) {
            return false;
        }

        if (advertisement.getRemainingBudget() <= 0) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        if (advertisement.getStartDate() != null &&
                advertisement.getStartDate().isAfter(now)) {
            return false;
        }

        if (advertisement.getEndDate() != null &&
                advertisement.getEndDate().isBefore(now)) {
            return false;
        }

        return true;
    }

}