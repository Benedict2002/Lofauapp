package com.codewithben.Lofau.admin.audit.service;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.admin.audit.dto.request.AuditFilterRequest;
import com.codewithben.Lofau.admin.audit.dto.response.AuditLogResponse;
import com.codewithben.Lofau.admin.audit.dto.response.AuditStatisticsResponse;
import com.codewithben.Lofau.admin.audit.enums.AuditAction;
import com.codewithben.Lofau.admin.audit.enums.AuditEntityType;

import java.util.List;
import java.util.UUID;

public interface AuditService {

    /**
     * ==========================================================
     * Create a new audit log.
     * This method will be called by every admin module.
     * ==========================================================
     */
    void log(
            AuditAction action,
            AuditEntityType entityType,
            UUID entityId,
            String entityName,
            String description,
            String reason
    );
    /**
     * ==========================================================
     * Get all audit logs.
     * ==========================================================
     */
    List<AuditLogResponse> getAllLogs();

    /**
     * ==========================================================
     * Get one audit log.
     * ==========================================================
     */
    AuditLogResponse getLog(UUID auditId);

    /**
     * ==========================================================
     * Filter audit logs.
     * ==========================================================
     */
    List<AuditLogResponse> filterLogs(
            AuditFilterRequest request
    );

    /**
     * ==========================================================
     * Get recent activity.
     * ==========================================================
     */
    List<AuditLogResponse> getRecentLogs();

    /**
     * ==========================================================
     * Dashboard statistics.
     * ==========================================================
     */
    AuditStatisticsResponse getStatistics();

}