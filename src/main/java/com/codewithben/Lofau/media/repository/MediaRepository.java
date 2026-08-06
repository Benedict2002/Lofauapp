package com.codewithben.Lofau.media.repository;

import com.codewithben.Lofau.media.entity.Media;
import com.codewithben.Lofau.media.enums.MediaPurpose;
import com.codewithben.Lofau.media.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {

    List<Media> findByOwnerIdAndOwnerTypeAndDeletedFalseOrderBySortOrderAsc(
            UUID ownerId,
            OwnerType ownerType
    );
    Optional<Media> findByOwnerIdAndOwnerTypeAndPurposeAndDeletedFalse(
            UUID ownerId,
            OwnerType ownerType,
            MediaPurpose purpose
    );


    List<Media> findByOwnerIdAndOwnerTypeAndPurposeAndDeletedFalseOrderBySortOrderAsc(
            UUID ownerId,
            OwnerType ownerType,
            MediaPurpose purpose
    );
    @Query("""
       SELECT COALESCE(SUM(m.fileSize), 0)
       FROM Media m
       WHERE m.deleted = false
       """)
    Long getTotalStorageUsed();

    /**
     * Counts uploaded media after the given date.
     */
    long countByCreatedAtAfter(LocalDateTime createdAt);
}
