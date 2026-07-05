package com.codewithben.Lofau.group.dto.response;

import com.codewithben.Lofau.group.enums.GroupCategory;
import com.codewithben.Lofau.group.enums.GroupStatus;
import com.codewithben.Lofau.group.enums.GroupVisibility;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class GroupResponse {

    private UUID id;

    private String name;

    private String slug;

    private String description;

    private String profileImageUrl;

    private String coverImageUrl;

    private GroupVisibility visibility;

    private GroupCategory category;

    private GroupStatus status;

    private String location;

    private Double latitude;

    private Double longitude;

    private Long ownerId;

    private String ownerUsername;

    private Integer memberCount;

    private Integer postCount;

    private Boolean verified;

    private LocalDateTime createdAt;

}