package com.codewithben.Lofau.advertisement.repository;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
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



    long countByDeletedFalse();
    List<Advertisement> findByApprovedTrueAndActiveFalseAndPausedFalseAndDeletedFalse();

    List<Advertisement> findByActiveTrueAndDeletedFalse();



    List<Advertisement> findByApprovedFalseAndDeletedFalse();



    // ===========================
// ADMIN DASHBOARD
// ===========================

    // Total advertisements
    long count();

    // Active advertisements
    long countByActiveTrue();

    // Inactive advertisements
    long countByActiveFalse();

    // Approved advertisements
    long countByApprovedTrue();

    // Pending approval
    long countByApprovedFalse();

    // Paused campaigns
    long countByPausedTrue();

    // Soft deleted advertisements
    long countByDeletedTrue();

    // Count by status
    long countByStatus(AdvertisementStatus status);

    @Query("""
       SELECT COALESCE(SUM(a.spentBudget),0)
       FROM Advertisement a
       WHERE a.deleted = false
       """)
    Long getTotalRevenue();


    /**
     * Sum of advertisement clicks today.
     */
    @Query("""
SELECT COALESCE(SUM(a.clicks),0)
FROM Advertisement a
WHERE a.updatedAt >= :today
""")
    Long sumClicksToday(
            @Param("today") LocalDateTime today
    );

    /**
     * Sum of advertisement impressions today.
     */
    @Query("""
SELECT COALESCE(SUM(a.impressions),0)
FROM Advertisement a
WHERE a.updatedAt >= :today
""")
    Long sumImpressionsToday(
            @Param("today") LocalDateTime today
    );

    /**
     * Average click-through rate.
     */
    @Query("""
SELECT COALESCE(AVG(
CASE
WHEN a.impressions = 0 THEN 0.0
ELSE (1.0 * a.clicks / a.impressions)
END
),0.0)
FROM Advertisement a
""")
    Double getAverageCtr();

    List<Advertisement> findByStatus(AdvertisementStatus status);
}