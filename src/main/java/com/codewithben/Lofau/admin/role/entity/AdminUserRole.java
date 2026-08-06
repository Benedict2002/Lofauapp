package com.codewithben.Lofau.admin.role.entity;

import com.codewithben.Lofau.User.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "admin_user_roles",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "user_id",
                                "role_id"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Admin User
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    /**
     * Admin Role
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "role_id",
            nullable = false
    )
    private AdminRole role;

    /**
     * Who granted this role
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    /**
     * Audit
     */
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();

    /**
     * Active?
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}