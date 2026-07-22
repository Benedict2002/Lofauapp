package com.codewithben.Lofau.report.dto.response;

import com.codewithben.Lofau.report.enums.ReportReason;
import com.codewithben.Lofau.report.enums.ReportStatus;
import com.codewithben.Lofau.report.enums.ReportTargetType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReportResponse {

    private UUID id;

    private UUID targetId;

    private ReportTargetType targetType;

    private ReportReason reason;

    private String description;

    private ReportStatus status;

    private LocalDateTime createdAt;
}