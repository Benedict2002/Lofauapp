package com.codewithben.Lofau.admin.audit.service.impl;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.User.userService.CurrentUserService;
import com.codewithben.Lofau.admin.audit.dto.request.AuditFilterRequest;
import com.codewithben.Lofau.admin.audit.dto.response.AuditLogResponse;
import com.codewithben.Lofau.admin.audit.dto.response.AuditStatisticsResponse;
import com.codewithben.Lofau.admin.audit.entity.AuditLog;
import com.codewithben.Lofau.admin.audit.enums.AuditAction;
import com.codewithben.Lofau.admin.audit.enums.AuditEntityType;
import com.codewithben.Lofau.admin.audit.mapper.AuditMapper;
import com.codewithben.Lofau.admin.audit.repository.AuditLogRepository;
import com.codewithben.Lofau.admin.audit.service.AuditService;
import com.codewithben.Lofau.admin.audit.specification.AuditSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    private final UserRepository userRepository;

    private final AuditMapper auditMapper;
    private final CurrentUserService currentUserService;

    private final HttpServletRequest request;

    /**
     * ==========================================================
     * Record an admin action.
     * ==========================================================
     */
    @Override
    public void log(
            AuditAction action,
            AuditEntityType entityType,
            UUID entityId,
            String entityName,
            String description,
            String reason
    ) {

        User admin = currentUserService.getCurrentUser();

        AuditLog auditLog = AuditLog.builder()

                .admin(admin)

                .action(action)

                .entityType(entityType)

                .entityId(entityId)

                .entityName(entityName)

                .description(description)

                .reason(reason)

                .ipAddress(request.getRemoteAddr())

                .userAgent(request.getHeader("User-Agent"))

                .createdAt(LocalDateTime.now())

                .build();

        auditLogRepository.save(auditLog);
    }

    /**
     * ==========================================================
     * Get all audit logs.
     * Ordered from newest to oldest.
     * ==========================================================
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAllLogs() {

        return auditLogRepository
                .findAllByOrderByCreatedAtDesc(
                        org.springframework.data.domain.Pageable.unpaged()
                )
                .stream()
                .map(auditMapper::toResponse)
                .toList();
    }

    /**
     * ==========================================================
     * Get a single audit log.
     * ==========================================================
     */
    @Override
    @Transactional(readOnly = true)
    public AuditLogResponse getLog(UUID auditId) {

        AuditLog auditLog = auditLogRepository.findById(auditId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Audit log not found."
                        ));

        return auditMapper.toResponse(auditLog);
    }

    /**
     * ==========================================================
     * Filter Audit Logs
     * ==========================================================
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> filterLogs(
            AuditFilterRequest request
    ) {

        return auditLogRepository

                .findAll(
                        AuditSpecification.filter(request)
                )

                .stream()

                .map(auditMapper::toResponse)

                .toList();
    }

    /**
     * ==========================================================
     * Get Recent Audit Logs
     * ==========================================================
     */
    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getRecentLogs() {

        return auditLogRepository

                .findTop20ByOrderByCreatedAtDesc()

                .stream()

                .map(auditMapper::toResponse)

                .toList();
    }

    /**
     * ==========================================================
     * Audit Dashboard Statistics
     * ==========================================================
     */
    @Override
    @Transactional(readOnly = true)
    public AuditStatisticsResponse getStatistics() {

        LocalDateTime today =
                LocalDateTime.now().toLocalDate().atStartOfDay();

        return AuditStatisticsResponse.builder()

                /*
                 * Overall Activity
                 */
                .totalLogs(
                        auditLogRepository.count()
                )

                .todayLogs(
                        auditLogRepository.countByCreatedAtAfter(today)
                )

                /*
                 * User Activity
                 */
                .userActions(
                        auditLogRepository.countByEntityType(
                                AuditEntityType.USER
                        )
                )

                /*
                 * Role Activity
                 */
                .roleActions(
                        auditLogRepository.countByEntityType(
                                AuditEntityType.ROLE
                        )
                )

                /*
                 * Post Activity
                 */
                .postActions(
                        auditLogRepository.countByEntityType(
                                AuditEntityType.POST
                        )
                )

                /*
                 * Advertisement Activity
                 */
                .advertisementActions(
                        auditLogRepository.countByEntityType(
                                AuditEntityType.ADVERTISEMENT
                        )
                )

                /*
                 * Report Activity
                 */
                .reportActions(
                        auditLogRepository.countByEntityType(
                                AuditEntityType.REPORT
                        )
                )

                /*
                 * Group Activity
                 */
                .groupActions(
                        auditLogRepository.countByEntityType(
                                AuditEntityType.GROUP
                        )
                )

                .build();
    }
}