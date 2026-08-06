package com.codewithben.Lofau.admin.role.mapper;

import com.codewithben.Lofau.admin.role.dto.response.AdminRoleResponse;
import com.codewithben.Lofau.admin.role.dto.response.AdminUserRoleResponse;
import com.codewithben.Lofau.admin.role.dto.response.AdminUserSummaryResponse;
import com.codewithben.Lofau.admin.role.dto.response.RoleMemberResponse;
import com.codewithben.Lofau.admin.role.entity.AdminRole;
import com.codewithben.Lofau.admin.role.entity.AdminUserRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminRoleMapper {

    /**
     * =====================================================
     * ROLE
     * =====================================================
     */
    public AdminRoleResponse toRoleResponse(AdminRole role) {

        return AdminRoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .active(role.getActive())
                .createdAt(role.getCreatedAt())
                .build();
    }

    /**
     * =====================================================
     * ROLE ASSIGNMENT
     * =====================================================
     */
    public AdminUserRoleResponse toUserRoleResponse(
            AdminUserRole assignment
    ) {

        return AdminUserRoleResponse.builder()
                .assignmentId(assignment.getId())
                .role(assignment.getRole().getName())
                .active(assignment.getActive())
                .assignedAt(assignment.getAssignedAt())
                .build();
    }

    /**
     * =====================================================
     * ROLE MEMBER
     * =====================================================
     */
    public RoleMemberResponse toRoleMemberResponse(
            AdminUserRole assignment
    ) {

        return RoleMemberResponse.builder()

                .assignmentId(assignment.getId())

                .userId(assignment.getUser().getId())

                .firstName(assignment.getUser().getFirstName())

                .lastName(assignment.getUser().getLastName())

                .username(assignment.getUser().getDisplayUsername())

                .email(assignment.getUser().getEmail())

                .verified(assignment.getUser().getVerified())

                .enabled(assignment.getUser().isEnabled())

                .active(assignment.getActive())

                .assignedAt(assignment.getAssignedAt())

                .build();
    }

    /**
     * =====================================================
     * ADMIN SUMMARY
     * =====================================================
     */
    public AdminUserSummaryResponse toAdminSummary(

            AdminUserRole assignment,

            List<AdminUserRoleResponse> roles

    ) {

        return AdminUserSummaryResponse.builder()

                .userId(assignment.getUser().getId())

                .firstName(assignment.getUser().getFirstName())

                .lastName(assignment.getUser().getLastName())

                .username(assignment.getUser().getDisplayUsername())

                .email(assignment.getUser().getEmail())

                .verified(assignment.getUser().getVerified())

                .enabled(assignment.getUser().isEnabled())

                .roles(roles)

                .build();
    }

}