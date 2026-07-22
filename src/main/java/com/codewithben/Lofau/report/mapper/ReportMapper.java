package com.codewithben.Lofau.report.mapper;

import com.codewithben.Lofau.report.dto.request.CreateReportRequest;
import com.codewithben.Lofau.report.dto.response.ReportResponse;
import com.codewithben.Lofau.report.entity.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public Report toEntity(CreateReportRequest request) {

        if (request == null) {
            return null;
        }

        return Report.builder()
                .targetId(request.getTargetId())
                .targetType(request.getTargetType())
                .reason(request.getReason())
                .description(request.getDescription())
                .build();
    }

    public ReportResponse toResponse(Report report) {

        if (report == null) {
            return null;
        }

        return ReportResponse.builder()
                .id(report.getId())
                .targetId(report.getTargetId())
                .targetType(report.getTargetType())
                .reason(report.getReason())
                .description(report.getDescription())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .build();
    }

}