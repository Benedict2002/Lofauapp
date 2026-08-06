package com.codewithben.Lofau.admin.role.service;

import com.codewithben.Lofau.admin.role.dto.request.AssignRoleRequest;
import com.codewithben.Lofau.admin.role.dto.request.RemoveRoleRequest;
import com.codewithben.Lofau.admin.role.dto.response.AdminRoleResponse;
import com.codewithben.Lofau.admin.role.dto.response.AdminUserSummaryResponse;
import com.codewithben.Lofau.admin.role.dto.response.AdminUserRoleResponse;
import com.codewithben.Lofau.admin.role.dto.response.RoleMemberResponse;
import com.codewithben.Lofau.admin.role.dto.response.RoleStatisticsResponse;
import com.codewithben.Lofau.admin.role.enums.AdminRoleName;

import java.util.List;
import java.util.UUID;

public interface RoleManagementService {

    /**
     * ============================================================
     * ROLE ASSIGNMENT
     * ============================================================
     */

    /**
     * Assigns an administrative role to a user.
     */
    void assignRole(AssignRoleRequest request);

    /**
     * Removes an administrative role from a user.
     */
    void removeRole(RemoveRoleRequest request);

    /**
     * ============================================================
     * USER ROLE MANAGEMENT
     * ============================================================
     */

    /**
     * Returns every active role assigned
     * to the specified user.
     */
    List<AdminUserRoleResponse> getUserRoles(UUID userId);

    /**
     * Returns every administrator in the system
     * together with the roles they possess.
     */
    List<AdminUserSummaryResponse> getAllAdmins();

    /**
     * Returns all users assigned to
     * a specific administrative role.
     */
    List<RoleMemberResponse> getRoleMembers(AdminRoleName roleName);

    /**
     * ============================================================
     * ROLE MANAGEMENT
     * ============================================================
     */

    /**
     * Returns every role that exists
     * in the system.
     */
    List<AdminRoleResponse> getAllRoles();

    /**
     * Activates a user's role assignment.
     */
    void activateRole(UUID assignmentId);

    /**
     * Deactivates a user's role assignment.
     */
    void deactivateRole(UUID assignmentId);

    /**
     * ============================================================
     * DASHBOARD
     * ============================================================
     */

    /**
     * Returns administrative role statistics.
     */
    RoleStatisticsResponse getStatistics();

}