package com.codewithben.Lofau.advertisement.validator;

import com.codewithben.Lofau.advertisement.entity.Advertisement;

public interface AdvertisementValidator {

    void validateForCreation(Advertisement advertisement);

    void validateForApproval(Advertisement advertisement);

    void validateForActivation(Advertisement advertisement);

    void validateForServing(Advertisement advertisement);

}