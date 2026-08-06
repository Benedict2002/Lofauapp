package com.codewithben.Lofau.admin.audit.repository;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.admin.audit.entity.AuditLog;
import com.codewithben.Lofau.admin.audit.enums.AuditAction;
import com.codewithben.Lofau.admin.audit.enums.AuditEntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, UUID>,
        JpaSpecificationExecutor<AuditLog> {

    /*
     * ==========================================================
     * Dashboard Statistics
     * ==========================================================
     */

    long countByAction(AuditAction action);

    long countByEntityType(AuditEntityType entityType);

    long countByCreatedAtAfter(LocalDateTime date);

    /*
     * ==========================================================
     * Admin History
     * ==========================================================
     */

    List<AuditLog> findByAdmin(User admin);

    Page<AuditLog> findByAdmin(User admin, Pageable pageable);

    /*
     * ==========================================================
     * Action History
     * ==========================================================
     */

    List<AuditLog> findByAction(AuditAction action);

    Page<AuditLog> findByAction(
            AuditAction action,
            Pageable pageable
    );

    /*
     * ==========================================================
     * Entity History
     * ==========================================================
     */

    List<AuditLog> findByEntityType(
            AuditEntityType entityType
    );

    Page<AuditLog> findByEntityType(
            AuditEntityType entityType,
            Pageable pageable
    );

    /*
     * ==========================================================
     * Entity Audit Trail
     * ==========================================================
     */

    List<AuditLog> findByEntityId(UUID entityId);

    /*
     * ==========================================================
     * Date Filtering
     * ==========================================================
     */

    List<AuditLog> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    Page<AuditLog> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    /*
     * ==========================================================
     * Recent Activity
     * ==========================================================
     */

    List<AuditLog> findTop20ByOrderByCreatedAtDesc();

    Page<AuditLog> findAllByOrderByCreatedAtDesc(
            Pageable pageable
    );



    long countByActionIn(List<AuditAction> actions);
    long countByActionAndCreatedAtAfter(
            AuditAction action,
            LocalDateTime createdAt
    );

}