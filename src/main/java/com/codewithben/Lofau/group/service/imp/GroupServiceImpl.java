package com.codewithben.Lofau.group.service.imp;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.group.dto.request.CreateGroupRequest;
import com.codewithben.Lofau.group.dto.response.GroupResponse;
import com.codewithben.Lofau.group.entity.Group;
import com.codewithben.Lofau.group.entity.GroupMember;
import com.codewithben.Lofau.group.enums.GroupRole;
import com.codewithben.Lofau.group.enums.MemberStatus;
import com.codewithben.Lofau.group.mapper.GroupMapper;
import com.codewithben.Lofau.group.repository.GroupMemberRepository;
import com.codewithben.Lofau.group.repository.GroupRepository;
import com.codewithben.Lofau.group.service.GroupService;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.service.MediaService;
import com.codewithben.Lofau.Util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final GroupMapper groupMapper;
    private final MediaService mediaService;

    @Override
    public GroupResponse createGroup(
            CreateGroupRequest request,
            MultipartFile profileImage,
            MultipartFile coverImage
    ) throws IOException {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (groupRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("A group with this name already exists.");
        }

        String slug = SlugUtil.generate(request.getName());

        if (groupRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }

        Group group = Group.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .visibility(request.getVisibility())
                .category(request.getCategory())
                .location(request.getLocation())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .createdBy(user)
                .memberCount(1)
                .postCount(0)
                .verified(false)
                .build();

        group = groupRepository.save(group);

        // Upload profile image
        if (profileImage != null && !profileImage.isEmpty()) {

            List<MediaResponse> mediaResponses =
                    mediaService.saveMedia(
                            group.getId(),
                            Collections.singletonList(profileImage),
                            OwnerType.GROUP
                    );

            if (!mediaResponses.isEmpty()) {
                group.setProfileImageUrl(mediaResponses.get(0).getUrl());
            }
        }

        // Upload cover image
        if (coverImage != null && !coverImage.isEmpty()) {

            List<MediaResponse> mediaResponses =
                    mediaService.saveMedia(
                            group.getId(),
                            Collections.singletonList(coverImage),
                            OwnerType.GROUP
                    );

            if (!mediaResponses.isEmpty()) {
                group.setCoverImageUrl(mediaResponses.get(0).getUrl());
            }
        }

        group = groupRepository.save(group);

        GroupMember owner = GroupMember.builder()
                .group(group)
                .user(user)
                .role(GroupRole.OWNER)
                .status(MemberStatus.APPROVED)
                .build();

        groupMemberRepository.save(owner);

        return groupMapper.toResponse(group);
    }

    @Override
    public List<GroupResponse> getAllGroups() {

        return groupRepository.findAll()
                .stream()
                .map(groupMapper::toResponse)
                .toList();
    }

    @Override
    public GroupResponse getGroupById(String id) {

        Group group = groupRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return groupMapper.toResponse(group);
    }
}