package com.codewithben.Lofau.report.repository;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.report.entity.Report;
import com.codewithben.Lofau.report.enums.ReportTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {

    boolean existsByReporterAndTargetIdAndTargetType(
            User reporter,
            UUID targetId,
            ReportTargetType targetType
    );
}