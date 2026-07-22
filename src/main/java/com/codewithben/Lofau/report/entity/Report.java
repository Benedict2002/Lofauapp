package com.codewithben.Lofau.report.entity;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.report.enums.ReportReason;

import com.codewithben.Lofau.report.enums.ReportStatus;
import com.codewithben.Lofau.report.enums.ReportTargetType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User reporter;

    @Column(nullable = false)
    private UUID targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportTargetType targetType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportReason reason;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReportStatus status = ReportStatus.PENDING;

    private Long reviewedBy;

    private LocalDateTime reviewedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}