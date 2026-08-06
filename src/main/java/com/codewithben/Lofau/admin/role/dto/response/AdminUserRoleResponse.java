package com.codewithben.Lofau.admin.role.dto.response;

import com.codewithben.Lofau.admin.role.enums.AdminRoleName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserRoleResponse {

    private UUID assignmentId;

    private UUID userId;

    private String fullName;

    private String email;

    private AdminRoleName role;

    private Boolean active;

    private LocalDateTime assignedAt;

}