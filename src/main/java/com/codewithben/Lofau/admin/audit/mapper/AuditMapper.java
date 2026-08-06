package com.codewithben.Lofau.admin.audit.mapper;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.admin.audit.dto.response.AuditLogResponse;
import com.codewithben.Lofau.admin.audit.entity.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper {

    /**
     * ==========================================================
     * Convert AuditLog Entity to AuditLogResponse
     * ==========================================================
     */
    public AuditLogResponse toResponse(AuditLog auditLog) {

        User admin = auditLog.getAdmin();

        return AuditLogResponse.builder()

                .id(auditLog.getId())

                // =====================================
                // Admin Information
                // =====================================
                .adminId(admin.getId())
                .adminName(
                        admin.getFirstName() + " " + admin.getLastName()
                )
                .adminEmail(admin.getEmail())

                // =====================================
                // Action
                // =====================================
                .action(auditLog.getAction())

                // =====================================
                // Entity
                // =====================================
                .entityType(auditLog.getEntityType())
                .entityId(auditLog.getEntityId())
                .entityName(auditLog.getEntityName())

                // =====================================
                // Details
                // =====================================
                .description(auditLog.getDescription())
                .reason(auditLog.getReason())

                // =====================================
                // Device Information
                // =====================================
                .ipAddress(auditLog.getIpAddress())
                .userAgent(auditLog.getUserAgent())

                // =====================================
                // Audit Time
                // =====================================
                .createdAt(auditLog.getCreatedAt())

                .build();
    }

}