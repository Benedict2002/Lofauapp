package com.codewithben.Lofau.group.entity;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.group.enums.GroupCategory;
import com.codewithben.Lofau.group.enums.GroupStatus;
import com.codewithben.Lofau.group.enums.GroupVisibility;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String slug;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupVisibility visibility;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupCategory category;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private GroupStatus status = GroupStatus.ACTIVE;

    private String location;

    private Double latitude;

    private Double longitude;

    /**
     * Contact Information
     */
    private String website;

    private String email;

    private String phoneNumber;

    /**
     * Group Rules
     */
    @Column(length = 3000)
    private String rules;

    /**
     * Group Settings
     */
    @Builder.Default
    private Boolean allowMemberPosts = true;

    @Builder.Default
    private Boolean requirePostApproval = false;

    @Builder.Default
    private Boolean allowMemberInvites = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Builder.Default
    private Integer memberCount = 1;

    @Builder.Default
    private Integer postCount = 0;

    @Builder.Default
    private Boolean verified = false;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<GroupMember> members = new ArrayList<>();
}