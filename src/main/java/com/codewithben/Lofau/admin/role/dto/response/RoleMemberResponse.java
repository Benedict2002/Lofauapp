package com.codewithben.Lofau.admin.role.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleMemberResponse {

    /**
     * Assignment
     */
    private UUID assignmentId;

    /**
     * User
     */
    private UUID userId;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    /**
     * Account
     */
    private Boolean verified;

    private Boolean enabled;

    /**
     * Role assignment
     */
    private Boolean active;

    private LocalDateTime assignedAt;

}