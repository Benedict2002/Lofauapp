package com.codewithben.Lofau.admin.audit.controller;

import com.codewithben.Lofau.admin.audit.dto.request.AuditFilterRequest;
import com.codewithben.Lofau.admin.audit.dto.response.AuditLogResponse;
import com.codewithben.Lofau.admin.audit.dto.response.AuditStatisticsResponse;
import com.codewithben.Lofau.admin.audit.service.AuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    /**
     * ==========================================================
     * Get all audit logs
     * ==========================================================
     */
    @GetMapping
    public List<AuditLogResponse> getAllLogs() {

        return auditService.getAllLogs();
    }

    /**
     * ==========================================================
     * Get audit log by ID
     * ==========================================================
     */
    @GetMapping("/{auditId}")
    public AuditLogResponse getLog(
            @PathVariable UUID auditId
    ) {

        return auditService.getLog(auditId);
    }

    /**
     * ==========================================================
     * Filter audit logs
     * ==========================================================
     */
    @PostMapping("/filter")
    public List<AuditLogResponse> filterLogs(
            @Valid @RequestBody AuditFilterRequest request
    ) {

        return auditService.filterLogs(request);
    }

    /**
     * ==========================================================
     * Get recent audit activity
     * ==========================================================
     */
    @GetMapping("/recent")
    public List<AuditLogResponse> getRecentLogs() {

        return auditService.getRecentLogs();
    }

    /**
     * ==========================================================
     * Get audit dashboard statistics
     * ==========================================================
     */
    @GetMapping("/statistics")
    public AuditStatisticsResponse getStatistics() {

        return auditService.getStatistics();
    }

}