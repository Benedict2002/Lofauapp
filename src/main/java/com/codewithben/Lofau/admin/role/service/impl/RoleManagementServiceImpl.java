package com.codewithben.Lofau.admin.role.service.impl;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.User.userService.CurrentUserService;
import com.codewithben.Lofau.admin.audit.enums.AuditAction;
import com.codewithben.Lofau.admin.audit.enums.AuditEntityType;
import com.codewithben.Lofau.admin.audit.service.AuditService;
import com.codewithben.Lofau.admin.role.dto.request.AssignRoleRequest;
import com.codewithben.Lofau.admin.role.dto.request.RemoveRoleRequest;
import com.codewithben.Lofau.admin.role.dto.response.*;
import com.codewithben.Lofau.admin.role.entity.AdminRole;
import com.codewithben.Lofau.admin.role.entity.AdminUserRole;
import com.codewithben.Lofau.admin.role.enums.AdminRoleName;
import com.codewithben.Lofau.admin.role.mapper.AdminRoleMapper;
import com.codewithben.Lofau.admin.role.repository.AdminRoleRepository;
import com.codewithben.Lofau.admin.role.repository.AdminUserRoleRepository;
import com.codewithben.Lofau.admin.role.service.RoleManagementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleManagementServiceImpl implements RoleManagementService {

    private final UserRepository userRepository;
    private final AuditService auditService;


    private final AdminRoleRepository adminRoleRepository;

    private final AdminUserRoleRepository adminUserRoleRepository;

    private final AdminRoleMapper adminRoleMapper;
    private final CurrentUserService currentUserService;
    User currentAdmin = currentUserService.getCurrentUser();

    @Override
    public void assignRole(AssignRoleRequest request) {

        User user = getUser(request.getUserId());

        AdminRole role = getRole(request.getRole());

        if (adminUserRoleRepository.existsByUserAndRole(user, role)) {
            return;
        }

        AdminUserRole userRole = AdminUserRole.builder()
                .user(user)
                .role(role)
                .active(true)
                .assignedAt(LocalDateTime.now())
                .build();

        adminUserRoleRepository.save(userRole);
        auditService.log(
                AuditAction.ROLE_ASSIGNED,
                AuditEntityType.ROLE,
                role.getId(),
                role.getName().name(),
                "Assigned role " + role.getName().name()
                        + " to " + user.getEmail(),
                null
        );
    }

    @Override
    public void removeRole(RemoveRoleRequest request) {

        User user = getUser(request.getUserId());

        AdminRole role = getRole(request.getRole());

        adminUserRoleRepository.deleteByUserAndRole(user, role);
        auditService.log(
                AuditAction.ROLE_REMOVED,
                AuditEntityType.ROLE,
                role.getId(),
                role.getName().name(),
                "Removed role " + role.getName().name() +
                        " from user " + user.getEmail(),
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserRoleResponse> getUserRoles(UUID userId) {

        User user = getUser(userId);

        return adminUserRoleRepository
                .findByUserAndActiveTrue(user)
                .stream()
                .map(adminRoleMapper::toUserRoleResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserSummaryResponse> getAllAdmins() {

        List<AdminUserRole> assignments =
                adminUserRoleRepository.findByActiveTrue();

        Map<UUID, AdminUserSummaryResponse> admins =
                new LinkedHashMap<>();

        for (AdminUserRole assignment : assignments) {

            UUID userId = assignment.getUser().getId();

            AdminUserSummaryResponse summary =
                    admins.computeIfAbsent(userId, id ->

                            adminRoleMapper.toAdminSummary(
                                    assignment,
                                    new java.util.ArrayList<>()
                            )
                    );

            summary.getRoles().add(
                    adminRoleMapper.toUserRoleResponse(assignment)
            );
        }

        return admins.values()
                .stream()
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleMemberResponse> getRoleMembers(
            AdminRoleName roleName
    ) {

        AdminRole role = getRole(roleName);

        return adminUserRoleRepository
                .findByRoleAndActiveTrue(role)
                .stream()
                .map(adminRoleMapper::toRoleMemberResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminRoleResponse> getAllRoles() {

        return adminRoleRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map(adminRoleMapper::toRoleResponse)
                .toList();
    }

    @Override
    public void activateRole(UUID assignmentId) {

        AdminUserRole assignment =
                adminUserRoleRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Role assignment not found"));

        assignment.setActive(true);

        adminUserRoleRepository.save(assignment);
        auditService.log(
                AuditAction.ROLE_ACTIVATED,
                AuditEntityType.ROLE,
                assignment.getRole().getId(),
                assignment.getRole().getName().name(),
                "Activated role assignment",
                null
        );
    }

    @Override
    public void deactivateRole(UUID assignmentId) {

        AdminUserRole assignment =
                adminUserRoleRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Role assignment not found"));

        assignment.setActive(false);

        adminUserRoleRepository.save(assignment);
        auditService.log(
                AuditAction.ROLE_DEACTIVATED,
                AuditEntityType.ROLE,
                assignment.getRole().getId(),
                assignment.getRole().getName().name(),
                "Deactivated role assignment",
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RoleStatisticsResponse getStatistics() {

        return RoleStatisticsResponse.builder()

                .superAdmins(
                        adminUserRoleRepository.countByRole(
                                getRole(AdminRoleName.SUPER_ADMIN)
                        )
                )

                .admins(
                        adminUserRoleRepository.countByRole(
                                getRole(AdminRoleName.ADMIN)
                        )
                )

                .contentModerators(
                        adminUserRoleRepository.countByRole(
                                getRole(AdminRoleName.CONTENT_MODERATOR)
                        )
                )

                .supportAdmins(
                        adminUserRoleRepository.countByRole(
                                getRole(AdminRoleName.SUPPORT_ADMIN)
                        )
                )

                .advertisementManagers(
                        adminUserRoleRepository.countByRole(
                                getRole(AdminRoleName.ADVERTISEMENT_MANAGER)
                        )
                )

                .analyticsAdmins(
                        adminUserRoleRepository.countByRole(
                                getRole(AdminRoleName.ANALYTICS_ADMIN)
                        )
                )

                .build();
    }

    /**
     * Get user by ID.
     */
    private User getUser(UUID userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));
    }

    /**
     * Get role by name.
     */
    private AdminRole getRole(AdminRoleName roleName) {

        return adminRoleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new EntityNotFoundException("Role not found"));
    }

}