package com.codewithben.Lofau.report.dto.request;

import com.codewithben.Lofau.report.enums.ReportReason;
import com.codewithben.Lofau.report.enums.ReportTargetType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateReportRequest {

    @NotNull
    private UUID targetId;

    @NotNull
    private ReportTargetType targetType;

    @NotNull
    private ReportReason reason;

    private String description;
}