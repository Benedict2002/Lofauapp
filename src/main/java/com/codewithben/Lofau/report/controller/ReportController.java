package com.codewithben.Lofau.report.controller;

import com.codewithben.Lofau.Auth.dto.response.ApiResponse;
import com.codewithben.Lofau.report.dto.request.CreateReportRequest;
import com.codewithben.Lofau.report.dto.response.ReportResponse;
import com.codewithben.Lofau.report.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReportResponse> createReport(
            @Valid @RequestBody CreateReportRequest request
    ) {

        return ApiResponse.success(

                "Report submitted successfully.",

                reportService.createReport(request)

        );

    }

}