package com.codewithben.Lofau.media.repository;

import com.codewithben.Lofau.media.entity.Media;
import com.codewithben.Lofau.media.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {

    List<Media> findByOwnerIdAndOwnerTypeAndDeletedFalseOrderBySortOrderAsc(
            UUID ownerId,
            OwnerType ownerType
    );

}
