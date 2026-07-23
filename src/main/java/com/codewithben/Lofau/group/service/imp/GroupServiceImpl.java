package com.codewithben.Lofau.group.service.imp;

import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.mapper.PostMapper;
import com.codewithben.Lofau.Post.repository.PostRepository;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private final PostRepository postRepository;
    private final PostMapper postMapper;


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

                .website(request.getWebsite())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())

                .rules(request.getRules())

                .allowMemberPosts(request.getAllowMemberPosts())
                .requirePostApproval(request.getRequirePostApproval())
                .allowMemberInvites(request.getAllowMemberInvites())

                .createdBy(user)

                .memberCount(1)
                .postCount(0)
                .verified(false)

                .build();

        group = groupRepository.save(group);

        /*
         * Upload Profile Image
         */
        if (profileImage != null && !profileImage.isEmpty()) {

            mediaService.uploadProfile(
                    group.getId(),
                    profileImage,
                    OwnerType.GROUP
            );
        }

        /*
         * Upload Cover Image
         */
        if (coverImage != null && !coverImage.isEmpty()) {

            mediaService.uploadCover(
                    group.getId(),
                    coverImage,
                    OwnerType.GROUP
            );
        }

        /*
         * Create Owner Membership
         */
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

        group.setMemberCount(
                (int) groupMemberRepository.countByGroupAndStatus(
                        group,
                        MemberStatus.APPROVED
                )
        );

        groupRepository.save(group);

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

        group.setMemberCount(
                (int) groupMemberRepository.countByGroupAndStatus(
                        group,
                        MemberStatus.APPROVED
                )
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
                        .username(member.getUser().getDisplayUsername())
                        .firstName(member.getUser().getFirstName())
                        .lastName(member.getUser().getLastName())
                        .profileImage(
                                mediaService.getProfile(
                                        member.getUser().getId(),
                                        OwnerType.USER
                                )
                        )
                        .role(member.getRole())
                        .status(member.getStatus())
                        .joinedAt(member.getJoinedAt())
                        .build())
                .toList();
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
                        .username(member.getUser().getDisplayUsername())
                        .firstName(member.getUser().getFirstName())
                        .lastName(member.getUser().getLastName())
                        .profileImage(
                                mediaService.getProfile(
                                        member.getUser().getId(),
                                        OwnerType.USER
                                )
                        )
                        .role(member.getRole())
                        .status(member.getStatus())
                        .joinedAt(member.getJoinedAt())
                        .build())
                .toList();
    }

    @Override
    public void approveJoinRequest(
            String groupId,
            UUID userId
    ) {

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
    public void rejectJoinRequest(
            String groupId,
            UUID userId
    ) {

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
            UUID userId
    ) {

        return groupMemberRepository
                .findByGroupIdAndUserId(
                        group.getId(),
                        userId
                )
                .orElseThrow(() ->
                        new RuntimeException("Member not found"));
    }


    @Override
    public void promoteToAdmin(
            String groupId,
            UUID userId
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
            UUID userId
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
            UUID userId
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
            UpdateGroupRequest request,
            MultipartFile profileImage,
            MultipartFile coverImage
    ) throws IOException {

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

        /*
         * Basic Information
         */
        if (request.getName() != null &&
                !request.getName().isBlank()) {

            group.setName(request.getName());

            String slug = SlugUtil.generate(request.getName());

            if (groupRepository.existsBySlug(slug)
                    && !slug.equals(group.getSlug())) {

                slug += "-" + System.currentTimeMillis();
            }

            group.setSlug(slug);
        }

        if (request.getDescription() != null) {
            group.setDescription(request.getDescription());
        }

        /*
         * Visibility
         */
        if (request.getVisibility() != null) {
            group.setVisibility(request.getVisibility());
        }

        if (request.getCategory() != null) {
            group.setCategory(request.getCategory());
        }

        /*
         * Location
         */
        if (request.getLocation() != null) {
            group.setLocation(request.getLocation());
        }

        if (request.getLatitude() != null) {
            group.setLatitude(request.getLatitude());
        }

        if (request.getLongitude() != null) {
            group.setLongitude(request.getLongitude());
        }

        /*
         * Contact Information
         */
        if (request.getWebsite() != null) {
            group.setWebsite(request.getWebsite());
        }

        if (request.getEmail() != null) {
            group.setEmail(request.getEmail());
        }

        if (request.getPhoneNumber() != null) {
            group.setPhoneNumber(request.getPhoneNumber());
        }

        /*
         * Rules
         */
        if (request.getRules() != null) {
            group.setRules(request.getRules());
        }

        /*
         * Settings
         */
        if (request.getAllowMemberPosts() != null) {
            group.setAllowMemberPosts(request.getAllowMemberPosts());
        }

        if (request.getRequirePostApproval() != null) {
            group.setRequirePostApproval(request.getRequirePostApproval());
        }

        if (request.getAllowMemberInvites() != null) {
            group.setAllowMemberInvites(request.getAllowMemberInvites());
        }

        /*
         * Images
         */
        if (profileImage != null && !profileImage.isEmpty()) {

            mediaService.uploadProfile(
                    group.getId(),
                    profileImage,
                    OwnerType.GROUP
            );
        }

        if (coverImage != null && !coverImage.isEmpty()) {

            mediaService.uploadCover(
                    group.getId(),
                    coverImage,
                    OwnerType.GROUP
            );
        }

        group.setUpdatedAt(LocalDateTime.now());

        group = groupRepository.save(group);

        return groupMapper.toResponse(group);
    }
    private GroupMember getAdminMember(
            Group group,
            User user
    ) {

        GroupMember member = groupMemberRepository
                .findByGroupAndUser(group, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "You are not a member of this group."
                        ));

        if (member.getRole() != GroupRole.OWNER
                && member.getRole() != GroupRole.ADMIN) {

            throw new RuntimeException(
                    "Only group owners or admins can perform this action."
            );
        }

        return member;
    }

    @Override
    public PostResponse pinPost(
            UUID groupId,
            UUID postId
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        // Verify user is OWNER or ADMIN
        getAdminMember(group, user);

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        // Ensure the post belongs to this group
        if (post.getGroup() == null
                || !post.getGroup().getId().equals(groupId)) {

            throw new RuntimeException(
                    "This post does not belong to this group."
            );
        }

        // Unpin any currently pinned post
        Optional<Post> existingPinned =
                postRepository.findByGroupAndPinnedTrueAndDeletedFalse(group);

        if (existingPinned.isPresent()) {

            Post pinnedPost = existingPinned.get();

            if (!pinnedPost.getId().equals(post.getId())) {

                pinnedPost.setPinned(false);
                pinnedPost.setPinnedAt(null);

                postRepository.save(pinnedPost);
            }
        }

        // If already pinned, return it
        if (Boolean.TRUE.equals(post.getPinned())) {
            return postMapper.toResponse(post);
        }

        post.setPinned(true);
        post.setPinnedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);

        return postMapper.toResponse(savedPost);
    }

    @Override
    public PostResponse unpinPost(
            UUID groupId,
            UUID postId
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));

        // Verify OWNER or ADMIN
        getAdminMember(group, user);

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post not found"));

        if (post.getGroup() == null
                || !post.getGroup().getId().equals(groupId)) {

            throw new RuntimeException(
                    "This post does not belong to this group."
            );
        }

        // Already unpinned
        if (!Boolean.TRUE.equals(post.getPinned())) {
            return postMapper.toResponse(post);
        }

        post.setPinned(false);
        post.setPinnedAt(null);

        post = postRepository.save(post);

        return postMapper.toResponse(post);
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