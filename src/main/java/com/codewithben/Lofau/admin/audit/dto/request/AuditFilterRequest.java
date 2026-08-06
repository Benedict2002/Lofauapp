package com.codewithben.Lofau.admin.audit.dto.request;

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
public class AuditFilterRequest {

    /*
     * Filter by admin
     */
    private UUID adminId;

    /*
     * Filter by action
     */
    private AuditAction action;

    /*
     * Filter by entity
     */
    private AuditEntityType entityType;

    /*
     * Date range
     */
    private LocalDateTime startDate;

    private LocalDateTime endDate;

}