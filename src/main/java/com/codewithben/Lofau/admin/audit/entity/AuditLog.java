package com.codewithben.Lofau.admin.audit.entity;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.admin.audit.enums.AuditAction;
import com.codewithben.Lofau.admin.audit.enums.AuditEntityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admin_audit_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /*
     * Admin who performed the action
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    /*
     * Action performed
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    /*
     * Entity affected
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditEntityType entityType;

    /*
     * UUID of affected entity
     */
    @Column(nullable = false)
    private UUID entityId;

    /*
     * Human readable name
     *
     * Example:
     * "Lost iPhone"
     * "John Doe"
     * "Advertisement #12"
     */
    @Column(length = 200)
    private String entityName;

    /*
     * Description
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /*
     * Optional reason
     */
    @Column(length = 500)
    private String reason;

    /*
     * Client IP
     */
    @Column(length = 100)
    private String ipAddress;

    /*
     * Browser / Device
     */
    @Column(length = 300)
    private String userAgent;

    /*
     * Timestamp
     */
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}