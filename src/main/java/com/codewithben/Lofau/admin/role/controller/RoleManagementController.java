package com.codewithben.Lofau.admin.role.controller;

import com.codewithben.Lofau.admin.role.dto.request.AssignRoleRequest;
import com.codewithben.Lofau.admin.role.dto.request.RemoveRoleRequest;
import com.codewithben.Lofau.admin.role.dto.response.AdminRoleResponse;
import com.codewithben.Lofau.admin.role.dto.response.AdminUserRoleResponse;
import com.codewithben.Lofau.admin.role.dto.response.AdminUserSummaryResponse;
import com.codewithben.Lofau.admin.role.dto.response.RoleMemberResponse;
import com.codewithben.Lofau.admin.role.dto.response.RoleStatisticsResponse;
import com.codewithben.Lofau.admin.role.enums.AdminRoleName;
import com.codewithben.Lofau.admin.role.service.RoleManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/roles")
@RequiredArgsConstructor
public class RoleManagementController {

    private final RoleManagementService roleManagementService;

    /**
     * ==========================================================
     * Assign an admin role to a user
     * ==========================================================
     */
    @PostMapping("/assign")
    @ResponseStatus(HttpStatus.OK)
    public void assignRole(
            @Valid @RequestBody AssignRoleRequest request
    ) {
        roleManagementService.assignRole(request);
    }

    /**
     * ==========================================================
     * Remove an admin role from a user
     * ==========================================================
     */
    @DeleteMapping("/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRole(
            @Valid @RequestBody RemoveRoleRequest request
    ) {
        roleManagementService.removeRole(request);
    }

    /**
     * ==========================================================
     * Get all roles assigned to one user
     * ==========================================================
     */
    @GetMapping("/user/{userId}")
    public List<AdminUserRoleResponse> getUserRoles(
            @PathVariable UUID userId
    ) {
        return roleManagementService.getUserRoles(userId);
    }

    /**
     * ==========================================================
     * Get every administrator in the system
     * ==========================================================
     */
    @GetMapping("/admins")
    public List<AdminUserSummaryResponse> getAllAdmins() {
        return roleManagementService.getAllAdmins();
    }

    /**
     * ==========================================================
     * Get members assigned to a specific role
     * ==========================================================
     */
    @GetMapping("/{roleName}/members")
    public List<RoleMemberResponse> getRoleMembers(
            @PathVariable AdminRoleName roleName
    ) {
        return roleManagementService.getRoleMembers(roleName);
    }

    /**
     * ==========================================================
     * Get all available admin roles
     * ==========================================================
     */
    @GetMapping
    public List<AdminRoleResponse> getAllRoles() {
        return roleManagementService.getAllRoles();
    }

    /**
     * ==========================================================
     * Activate a role assignment
     * ==========================================================
     */
    @PatchMapping("/{assignmentId}/activate")
    @ResponseStatus(HttpStatus.OK)
    public void activateRole(
            @PathVariable UUID assignmentId
    ) {
        roleManagementService.activateRole(assignmentId);
    }

    /**
     * ==========================================================
     * Deactivate a role assignment
     * ==========================================================
     */
    @PatchMapping("/{assignmentId}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public void deactivateRole(
            @PathVariable UUID assignmentId
    ) {
        roleManagementService.deactivateRole(assignmentId);
    }

    /**
     * ==========================================================
     * Dashboard statistics for admin roles
     * ==========================================================
     */
    @GetMapping("/statistics")
    public RoleStatisticsResponse getStatistics() {
        return roleManagementService.getStatistics();
    }
}