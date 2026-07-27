package com.codewithben.Lofau.advertisement.validator;

import com.codewithben.Lofau.advertisement.entity.Advertisement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvertisementValidatorImpl
        implements AdvertisementValidator {

    @Override
    public void validateForCreation(
            Advertisement advertisement
    ) {

        if (advertisement.getTitle() == null
                || advertisement.getTitle().isBlank()) {
            throw new RuntimeException("Advertisement title is required.");
        }

        if (advertisement.getType() == null) {
            throw new RuntimeException("Advertisement type is required.");
        }

        if (advertisement.getPlacement() == null) {
            throw new RuntimeException("Advertisement placement is required.");
        }

        if (advertisement.getAdvertiser() == null) {
            throw new RuntimeException("Advertiser is required.");
        }

        if (advertisement.getTotalBudget() < 0) {
            throw new RuntimeException("Budget cannot be negative.");
        }

        if (advertisement.getPriority() < 0) {
            throw new RuntimeException("Priority cannot be negative.");
        }

    }

    @Override
    public void validateForApproval(
            Advertisement advertisement
    ) {

        validateForCreation(advertisement);

        if (advertisement.getApproved()) {
            throw new RuntimeException("Advertisement is already approved.");
        }

    }

    @Override
    public void validateForActivation(
            Advertisement advertisement
    ) {

        if (!advertisement.getApproved()) {
            throw new RuntimeException("Advertisement must be approved first.");
        }

        if (advertisement.getRemainingBudget() <= 0) {
            throw new RuntimeException("Campaign has no remaining budget.");
        }

        if (advertisement.isExpired()) {
            throw new RuntimeException("Campaign has expired.");
        }

    }

    @Override
    public void validateForServing(
            Advertisement advertisement
    ) {

        validateForActivation(advertisement);

        if (!advertisement.getActive()) {
            throw new RuntimeException("Advertisement is inactive.");
        }

        if (advertisement.getPaused()) {
            throw new RuntimeException("Daily budget exhausted.");
        }

        if (!advertisement.hasStarted()) {
            throw new RuntimeException("Campaign has not started.");
        }

    }

}