package com.codewithben.Lofau.advertisement.repository;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AdvertisementRepository
        extends JpaRepository<Advertisement, UUID> {

    List<Advertisement> findByDeletedFalse();

    List<Advertisement> findByAdvertiserAndDeletedFalse(
            User advertiser
    );

    List<Advertisement> findByPlacementAndDeletedFalse(
            AdvertisementPlacement placement
    );

    List<Advertisement> findByStatusAndDeletedFalse(
            AdvertisementStatus status
    );

    List<Advertisement> findByApprovedTrueAndDeletedFalse();

    List<Advertisement> findByApprovedTrueAndActiveTrueAndDeletedFalse();

    List<Advertisement> findByApprovedTrueAndActiveTrueAndPlacementAndDeletedFalse(
            AdvertisementPlacement placement
    );

    List<Advertisement> findByEndDateBeforeAndDeletedFalse(
            LocalDateTime date
    );

    List<Advertisement> findByStartDateAfterAndDeletedFalse(
            LocalDateTime date
    );

    List<Advertisement> findByAdvertiser(
            User advertiser
    );

    long countByAdvertiser(
            User advertiser
    );

    long countByApprovedTrue();

    long countByActiveTrue();

    long countByDeletedFalse();
    List<Advertisement> findByApprovedTrueAndActiveFalseAndPausedFalseAndDeletedFalse();

    List<Advertisement> findByActiveTrueAndDeletedFalse();



    List<Advertisement> findByApprovedFalseAndDeletedFalse();




}