package com.codewithben.Lofau.admin.dashboard.controller;

import com.codewithben.Lofau.admin.dashboard.dto.response.AdminDashboardResponse;
import com.codewithben.Lofau.admin.dashboard.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Administrative dashboard endpoints.
 *
 * Provides a high-level overview of the platform,
 * including users, advertisements, posts,
 * reports and system statistics.
 */
@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    /**
     * Returns the administrator dashboard.
     */
    @GetMapping
    public AdminDashboardResponse getDashboard() {

        return adminDashboardService.getDashboard();
    }

}