package com.codewithben.Lofau.group.mapper;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.group.dto.response.GroupResponse;
import com.codewithben.Lofau.group.entity.Group;
import com.codewithben.Lofau.group.entity.GroupMember;
import com.codewithben.Lofau.group.enums.GroupRole;
import com.codewithben.Lofau.group.repository.GroupMemberRepository;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupMapper {

    private final MediaService mediaService;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    public GroupResponse toResponse(Group group) {

        boolean joined = false;
        GroupRole myRole = null;

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {

            Optional<User> currentUser =
                    userRepository.findByEmail(authentication.getName());

            if (currentUser.isPresent()) {

                Optional<GroupMember> membership =
                        groupMemberRepository.findByGroupAndUser(
                                group,
                                currentUser.get()
                        );

                if (membership.isPresent()) {
                    joined = true;
                    myRole = membership.get().getRole();
                }
            }
        }

        return GroupResponse.builder()

                /*
                 * Basic Information
                 */
                .id(group.getId())
                .name(group.getName())
                .slug(group.getSlug())
                .description(group.getDescription())

                /*
                 * Images
                 */
                .profileImage(
                        mediaService.getProfile(
                                group.getId(),
                                OwnerType.GROUP
                        )
                )
                .coverImage(
                        mediaService.getCover(
                                group.getId(),
                                OwnerType.GROUP
                        )
                )
                .gallery(
                        mediaService.getGallery(
                                group.getId(),
                                OwnerType.GROUP
                        )
                )

                /*
                 * Visibility
                 */
                .visibility(group.getVisibility())
                .category(group.getCategory())
                .status(group.getStatus())

                /*
                 * Location
                 */
                .location(group.getLocation())
                .latitude(group.getLatitude())
                .longitude(group.getLongitude())

                /*
                 * Contact
                 */
                .website(group.getWebsite())
                .email(group.getEmail())
                .phoneNumber(group.getPhoneNumber())

                /*
                 * Rules
                 */
                .rules(group.getRules())

                /*
                 * Settings
                 */
                .allowMemberPosts(group.getAllowMemberPosts())
                .requirePostApproval(group.getRequirePostApproval())
                .allowMemberInvites(group.getAllowMemberInvites())

                /*
                 * Owner
                 */
                .ownerId(group.getCreatedBy().getId())
                .ownerUsername(group.getCreatedBy().getDisplayUsername())

                /*
                 * Statistics
                 */
                .memberCount(group.getMemberCount())
                .postCount(group.getPostCount())
                .verified(group.getVerified())

                /*
                 * Current User
                 */
                .joined(joined)
                .myRole(myRole)

                .owner(myRole == GroupRole.OWNER)

                .admin(
                        myRole == GroupRole.OWNER
                                || myRole == GroupRole.ADMIN
                )

                .moderator(myRole == GroupRole.MODERATOR)

                /*
                 * Dates
                 */
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())

                .build();
    }
}