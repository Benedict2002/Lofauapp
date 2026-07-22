package com.codewithben.Lofau.report.service;

import com.codewithben.Lofau.report.dto.request.CreateReportRequest;
import com.codewithben.Lofau.report.dto.response.ReportResponse;

public interface ReportService {

    ReportResponse createReport(CreateReportRequest request);

}