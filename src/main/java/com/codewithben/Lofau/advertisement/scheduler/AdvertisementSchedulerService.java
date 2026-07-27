package com.codewithben.Lofau.advertisement.scheduler;

public interface AdvertisementSchedulerService {

    void activateAdvertisements();

    void expireAdvertisements();

    void pauseBudgetExhaustedAdvertisements();

    void resetDailyBudget();

}