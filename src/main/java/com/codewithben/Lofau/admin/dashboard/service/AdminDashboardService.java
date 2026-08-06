package com.codewithben.Lofau.admin.dashboard.service;

import com.codewithben.Lofau.admin.dashboard.dto.response.AdminDashboardResponse;

public interface AdminDashboardService {

    /**
     * Returns the admin dashboard summary.
     */
    AdminDashboardResponse getDashboard();

}