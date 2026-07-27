package com.codewithben.Lofau.advertisement.scheduler;

import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import com.codewithben.Lofau.advertisement.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdvertisementSchedulerServiceImpl
        implements AdvertisementSchedulerService {

    private final AdvertisementRepository advertisementRepository;

    /**
     * Automatically activates advertisements whose start date has arrived.
     */
    @Override
    public void activateAdvertisements() {

        log.info("Checking advertisements for activation...");

        List<Advertisement> advertisements =
                advertisementRepository
                        .findByApprovedTrueAndActiveFalseAndPausedFalseAndDeletedFalse();

        advertisements = advertisements.stream()

                .filter(ad -> ad.getStartDate() != null)

                .filter(ad ->
                        !ad.getStartDate().isAfter(LocalDateTime.now())
                )

                .toList();

        advertisements.forEach(ad -> {
            ad.setActive(true);
            ad.setStatus(AdvertisementStatus.ACTIVE);
        });

        advertisementRepository.saveAll(advertisements);

        log.info("{} advertisements activated.", advertisements.size());
    }

    /**
     * Automatically expires advertisements.
     */
    @Override
    public void expireAdvertisements() {

        log.info("Checking expired advertisements...");

        List<Advertisement> advertisements =
                advertisementRepository
                        .findByActiveTrueAndDeletedFalse();

        advertisements = advertisements.stream()

                .filter(ad -> ad.getEndDate() != null)

                .filter(ad ->
                        ad.getEndDate().isBefore(LocalDateTime.now())
                )

                .toList();

        advertisements.forEach(ad -> {

            ad.setActive(false);
            ad.setPaused(false);
            ad.setStatus(AdvertisementStatus.EXPIRED);

        });

        advertisementRepository.saveAll(advertisements);

        log.info("{} advertisements expired.", advertisements.size());
    }

    /**
     * Pause advertisements whose budgets are exhausted.
     */
    @Override
    public void pauseBudgetExhaustedAdvertisements() {

        log.info("Checking advertisement budgets...");

        List<Advertisement> advertisements =
                advertisementRepository
                        .findByActiveTrueAndDeletedFalse();

        advertisements = advertisements.stream()

                .filter(Advertisement::isBudgetExhausted)

                .toList();

        advertisements.forEach(ad -> {

            ad.setPaused(true);
            ad.setActive(false);
            ad.setStatus(AdvertisementStatus.PAUSED);

        });

        advertisementRepository.saveAll(advertisements);

        log.info("{} advertisements paused.", advertisements.size());
    }

    /**
     * Resets every advertisement's daily spending.
     */
    @Override
    public void resetDailyBudget() {

        log.info("Resetting daily advertisement budgets...");

        List<Advertisement> advertisements =
                advertisementRepository
                        .findByDeletedFalse();

        advertisements.forEach(ad -> {

            ad.setDailySpent(0);

            if (ad.getPaused()
                    && !ad.isBudgetExhausted()
                    && ad.getApproved()) {

                ad.setPaused(false);
                ad.setActive(true);
                ad.setStatus(AdvertisementStatus.ACTIVE);

            }

        });

        advertisementRepository.saveAll(advertisements);

        log.info("Daily budgets reset successfully.");
    }

}