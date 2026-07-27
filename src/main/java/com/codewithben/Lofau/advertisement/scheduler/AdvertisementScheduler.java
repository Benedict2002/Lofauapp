package com.codewithben.Lofau.advertisement.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdvertisementScheduler {

    private final AdvertisementSchedulerService schedulerService;

    /**
     * Every 5 minutes
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void activateAdvertisements() {

        schedulerService.activateAdvertisements();
    }

    /**
     * Every 10 minutes
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void expireAdvertisements() {

        schedulerService.expireAdvertisements();
    }

    /**
     * Every hour
     */
    @Scheduled(cron = "0 0 * * * *")
    public void pauseBudgetAds() {

        schedulerService.pauseBudgetExhaustedAdvertisements();
    }

    /**
     * Midnight every day
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void resetBudget() {

        schedulerService.resetDailyBudget();
    }

}