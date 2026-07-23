package com.codewithben.Lofau.group.dto.response;

import com.codewithben.Lofau.group.enums.GroupCategory;
import com.codewithben.Lofau.group.enums.GroupRole;
import com.codewithben.Lofau.group.enums.GroupStatus;
import com.codewithben.Lofau.group.enums.GroupVisibility;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class GroupResponse {

    private UUID id;

    private String name;

    private String slug;

    private String description;

    /*
     * Images
     */
    private MediaResponse profileImage;

    private MediaResponse coverImage;

    private List<MediaResponse> gallery;

    /*
     * Visibility
     */
    private GroupVisibility visibility;

    private GroupCategory category;

    private GroupStatus status;

    /*
     * Location
     */
    private String location;

    private Double latitude;

    private Double longitude;

    /*
     * Contact Information
     */
    private String website;

    private String email;

    private String phoneNumber;

    /*
     * Group Rules
     */
    private String rules;

    /*
     * Settings
     */
    private Boolean allowMemberPosts;

    private Boolean requirePostApproval;

    private Boolean allowMemberInvites;

    /*
     * Owner
     */
    private UUID ownerId;

    private String ownerUsername;

    /*
     * Statistics
     */
    private Integer memberCount;

    private Integer postCount;

    private Boolean verified;

    /*
     * Current User
     */
    private Boolean joined;

    private GroupRole myRole;

    /*
     * Dates
     */
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /*
     * Current User
     */

    private Boolean owner;

    private Boolean admin;

    private Boolean moderator;
}