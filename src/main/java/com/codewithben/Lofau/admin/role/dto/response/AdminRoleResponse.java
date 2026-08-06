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
public class AdminRoleResponse {

    private UUID id;

    private AdminRoleName name;

    private String description;

    private Boolean active;

    private LocalDateTime createdAt;

}