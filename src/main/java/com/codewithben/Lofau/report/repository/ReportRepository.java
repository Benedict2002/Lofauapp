package com.codewithben.Lofau.report.repository;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.report.entity.Report;
import com.codewithben.Lofau.report.enums.ReportStatus;
import com.codewithben.Lofau.report.enums.ReportTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {

    boolean existsByReporterAndTargetIdAndTargetType(
            User reporter,
            UUID targetId,
            ReportTargetType targetType
    );

    /**
     * Counts reports by status.
     */
    long countByStatus(ReportStatus status);

    /**
     * Counts reports created after the given date.
     */
    long countByCreatedAtAfter(LocalDateTime createdAt);
}