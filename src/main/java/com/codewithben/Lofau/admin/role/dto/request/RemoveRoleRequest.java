package com.codewithben.Lofau.admin.role.dto.request;

import com.codewithben.Lofau.admin.role.enums.AdminRoleName;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveRoleRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private AdminRoleName role;

}