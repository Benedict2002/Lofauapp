package com.codewithben.Lofau.group.mapper;

import com.codewithben.Lofau.group.dto.response.GroupResponse;
import com.codewithben.Lofau.group.entity.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

    public GroupResponse toResponse(Group group) {

        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .slug(group.getSlug())
                .description(group.getDescription())
                .profileImageUrl(group.getProfileImageUrl())
                .coverImageUrl(group.getCoverImageUrl())
                .visibility(group.getVisibility())
                .category(group.getCategory())
                .status(group.getStatus())
                .location(group.getLocation())
                .latitude(group.getLatitude())
                .longitude(group.getLongitude())
                .ownerId(group.getCreatedBy().getId())
                .ownerUsername(group.getCreatedBy().getUsername())
                .memberCount(group.getMemberCount())
                .postCount(group.getPostCount())
                .verified(group.getVerified())
                .createdAt(group.getCreatedAt())
                .build();
    }

}