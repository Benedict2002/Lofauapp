package com.codewithben.Lofau.admin.audit.dto.response;

import com.codewithben.Lofau.admin.audit.enums.AuditAction;
import com.codewithben.Lofau.admin.audit.enums.AuditEntityType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {

    private UUID id;

    /*
     * Admin information
     */
    private UUID adminId;

    private String adminName;

    private String adminEmail;

    /*
     * Action
     */
    private AuditAction action;

    /*
     * Target
     */
    private AuditEntityType entityType;

    private UUID entityId;

    private String entityName;

    /*
     * Details
     */
    private String description;

    private String reason;

    /*
     * Device
     */
    private String ipAddress;

    private String userAgent;

    /*
     * Time
     */
    private LocalDateTime createdAt;

}