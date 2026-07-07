package com.codewithben.Lofau.group.service.imp;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.group.dto.request.CreateGroupRequest;
import com.codewithben.Lofau.group.dto.request.UpdateGroupRequest;
import com.codewithben.Lofau.group.dto.response.GroupMemberResponse;
import com.codewithben.Lofau.group.dto.response.GroupResponse;
import com.codewithben.Lofau.group.entity.Group;
import com.codewithben.Lofau.group.entity.GroupMember;
import com.codewithben.Lofau.group.enums.GroupRole;
import com.codewithben.Lofau.group.enums.GroupVisibility;
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
import java.util.stream.Collectors;

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

    private GroupMember getCurrentMember(Group group) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new RuntimeException("You are not a member of this group"));

    }

    private void validateGroupAdmin(Group group) {

        GroupMember currentMember = getCurrentMember(group);

        if (currentMember.getRole() != GroupRole.OWNER &&
                currentMember.getRole() != GroupRole.ADMIN) {

            throw new RuntimeException(
                    "Only OWNER or ADMIN can perform this action."
            );
        }

    }

    @Override
    public GroupResponse joinGroup(String groupId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (groupMemberRepository.existsByGroupAndUser(group, user)) {
            throw new RuntimeException("You are already a member of this group.");
        }

        MemberStatus status = group.getVisibility() == GroupVisibility.PUBLIC
                ? MemberStatus.APPROVED
                : MemberStatus.PENDING;

        GroupMember member = GroupMember.builder()
                .group(group)
                .user(user)
                .role(GroupRole.MEMBER)
                .status(status)
                .build();

        groupMemberRepository.save(member);

        if (status == MemberStatus.APPROVED) {
            group.setMemberCount((int) groupMemberRepository.countByGroupAndStatus(
                    group,
                    MemberStatus.APPROVED
            ));
            groupRepository.save(group);
        }

        return groupMapper.toResponse(group);
    }

    @Override
    public void leaveGroup(String groupId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new RuntimeException("Group not found"));

        GroupMember member = groupMemberRepository
                .findByGroupAndUser(group, user)
                .orElseThrow(() ->
                        new RuntimeException("You are not a member of this group."));

        if (member.getRole() == GroupRole.OWNER) {
            throw new RuntimeException(
                    "Group owner cannot leave the group."
            );
        }

        groupMemberRepository.delete(member);

        group.setMemberCount(
                Math.max(0, group.getMemberCount() - 1)
        );

        groupRepository.save(group);
    }

    @Override
    public List<GroupMemberResponse> getMembers(String groupId) {

        List<GroupMember> members =
                groupMemberRepository.findByGroupIdAndStatus(
                        UUID.fromString(groupId),
                        MemberStatus.APPROVED
                );

        return members.stream()
                .map(member -> GroupMemberResponse.builder()
                        .userId(member.getUser().getId())
                        .username(member.getUser().getUsername())
                        .firstName(member.getUser().getFirstName())
                        .lastName(member.getUser().getLastName())
                        .profileImage(member.getUser().getProfilePictureUrl())
                        .role(member.getRole())
                        .status(member.getStatus())
                        .joinedAt(member.getJoinedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupMemberResponse> getPendingRequests(String groupId) {

        Group group = groupRepository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<GroupMember> requests =
                groupMemberRepository.findByGroupAndStatus(
                        group,
                        MemberStatus.PENDING
                );

        return requests.stream()
                .map(member -> GroupMemberResponse.builder()
                        .userId(member.getUser().getId())
                        .username(member.getUser().getUsername())
                        .firstName(member.getUser().getFirstName())
                        .lastName(member.getUser().getLastName())
                        .profileImage(member.getUser().getProfilePictureUrl())
                        .role(member.getRole())
                        .status(member.getStatus())
                        .joinedAt(member.getJoinedAt())
                        .build())
                .toList();
    }

    @Override
    public void approveJoinRequest(String groupId, Long userId) {

        Group group = groupRepository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new RuntimeException("Group not found"));

        validateGroupAdmin(group);

        GroupMember member = groupMemberRepository
                .findByGroupIdAndUserId(group.getId(), userId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        member.setStatus(MemberStatus.APPROVED);

        groupMemberRepository.save(member);

        group.setMemberCount(
                (int) groupMemberRepository.countByGroupAndStatus(
                        group,
                        MemberStatus.APPROVED
                )
        );

        groupRepository.save(group);

    }

    @Override
    public void rejectJoinRequest(String groupId, Long userId) {

        Group group = groupRepository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new RuntimeException("Group not found"));

        validateGroupAdmin(group);

        GroupMember member = groupMemberRepository
                .findByGroupIdAndUserId(group.getId(), userId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        groupMemberRepository.delete(member);

    }
    private GroupMember getMember(
            Group group,
            Long userId
    ) {

        return groupMemberRepository
                .findByGroupIdAndUserId(group.getId(), userId)
                .orElseThrow(() ->
                        new RuntimeException("Member not found"));

    }

    @Override
    public void promoteToAdmin(
            String groupId,
            Long userId
    ) {

        Group group = groupRepository.findById(
                        UUID.fromString(groupId))
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        validateGroupAdmin(group);

        GroupMember member = getMember(group, userId);

        if (member.getRole() == GroupRole.OWNER) {
            throw new RuntimeException(
                    "Owner cannot be promoted."
            );
        }

        member.setRole(GroupRole.ADMIN);

        groupMemberRepository.save(member);

    }

    @Override
    public void demoteAdmin(
            String groupId,
            Long userId
    ) {

        Group group = groupRepository.findById(
                        UUID.fromString(groupId))
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        validateGroupAdmin(group);

        GroupMember member = getMember(group, userId);

        if (member.getRole() == GroupRole.OWNER) {
            throw new RuntimeException(
                    "Owner cannot be demoted."
            );
        }

        member.setRole(GroupRole.MEMBER);

        groupMemberRepository.save(member);

    }

    @Override
    public void removeMember(
            String groupId,
            Long userId
    ) {

        Group group = groupRepository.findById(
                        UUID.fromString(groupId))
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        validateGroupAdmin(group);

        GroupMember member = getMember(group, userId);

        if (member.getRole() == GroupRole.OWNER) {
            throw new RuntimeException(
                    "Owner cannot be removed."
            );
        }

        groupMemberRepository.delete(member);

        group.setMemberCount(
                (int) groupMemberRepository.countByGroupAndStatus(
                        group,
                        MemberStatus.APPROVED
                )
        );

        groupRepository.save(group);

    }

    @Override
    public GroupResponse updateGroup(
            String groupId,
            UpdateGroupRequest request
    ) {

        Group group = groupRepository.findById(
                        UUID.fromString(groupId))
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        GroupMember currentMember = getCurrentMember(group);

        if (currentMember.getRole() != GroupRole.OWNER &&
                currentMember.getRole() != GroupRole.ADMIN) {

            throw new RuntimeException(
                    "Only OWNER or ADMIN can update this group."
            );
        }

        if (request.getName() != null &&
                !request.getName().isBlank()) {

            group.setName(request.getName());

            String slug = SlugUtil.generate(request.getName());

            if (groupRepository.existsBySlug(slug)
                    && !slug.equals(group.getSlug())) {

                slug = slug + "-" + System.currentTimeMillis();

            }

            group.setSlug(slug);

        }

        if (request.getDescription() != null)
            group.setDescription(request.getDescription());

        if (request.getVisibility() != null)
            group.setVisibility(request.getVisibility());

        if (request.getCategory() != null)
            group.setCategory(request.getCategory());

        if (request.getLocation() != null)
            group.setLocation(request.getLocation());

        if (request.getLatitude() != null)
            group.setLatitude(request.getLatitude());

        if (request.getLongitude() != null)
            group.setLongitude(request.getLongitude());

        groupRepository.save(group);

        return groupMapper.toResponse(group);

    }

    @Override
    public void deleteGroup(String groupId) {

        Group group = groupRepository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new RuntimeException("Group not found"));

        GroupMember currentMember = getCurrentMember(group);

        if (currentMember.getRole() != GroupRole.OWNER) {
            throw new RuntimeException("Only the owner can delete this group.");
        }

        groupRepository.delete(group);
    }
}