package com.codewithben.Lofau.admin.role.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserSummaryResponse {

    /**
     * User Information
     */
    private UUID userId;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    /**
     * User Account
     */
    private Boolean verified;

    private Boolean enabled;

    /**
     * Assigned Roles
     */
    private List<AdminUserRoleResponse> roles;

}