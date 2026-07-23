package com.codewithben.Lofau.Post.mapper;

import com.codewithben.Lofau.Post.dto.request.CreatePostRequest;
import com.codewithben.Lofau.Post.dto.request.UpdatePostRequest;
import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.repository.PostLikeRepository;
import com.codewithben.Lofau.Post.repository.SavedPostRepository;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final MediaService mediaService;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final SavedPostRepository savedPostRepository;


    private boolean isSavedByCurrentUser(Post post) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .map(user ->
                        savedPostRepository.existsByUserAndPost(user, post)
                )
                .orElse(false);
    }

    public void updateEntity(
            Post post,
            UpdatePostRequest request
    ) {

        if (request.getTitle() != null) {
            post.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            post.setDescription(request.getDescription());
        }

        if (request.getCategory() != null) {
            post.setCategory(request.getCategory());
        }

        if (request.getLocationName() != null) {
            post.setLocationName(request.getLocationName());
        }

        if (request.getLatitude() != null) {
            post.setLatitude(request.getLatitude());
        }

        if (request.getLongitude() != null) {
            post.setLongitude(request.getLongitude());
        }

        if (request.getRewardAmount() != null) {
            post.setRewardAmount(request.getRewardAmount());
        }

        if (request.getAnonymous() != null) {
            post.setAnonymous(request.getAnonymous());
        }

    }
    private boolean isLikedByCurrentUser(Post post) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .map(user -> postLikeRepository.existsByPostAndUser(post, user))
                .orElse(false);
    }

    public Post toEntity(CreatePostRequest request) {

        if (request == null) {
            return null;
        }

        return Post.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .postType(request.getPostType())
                .category(request.getCategory())
                .locationName(request.getLocationName())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .rewardAmount(request.getRewardAmount())
                .anonymous(request.getAnonymous())
                .build();
    }

    public PostResponse toResponse(Post post) {

        if (post == null) {
            return null;
        }

        List<MediaResponse> media =
                mediaService.getGallery(
                        post.getId(),
                        OwnerType.POST
                );

        MediaResponse previewImage =
                media.isEmpty() ? null : media.get(0);

        return PostResponse.builder()
                .id(post.getId())

                .userId(post.getUser().getId())
                .username(post.getUser().getDisplayUsername())

                // NEW
                .userProfileImage(
                        mediaService.getProfile(
                                post.getUser().getId(),
                                OwnerType.USER
                        )
                )

                // NEW
                .previewImage(previewImage)

                .groupId(
                        post.getGroup() != null
                                ? post.getGroup().getId()
                                : null
                )
                .groupName(
                        post.getGroup() != null
                                ? post.getGroup().getName()
                                : null
                )

                .title(post.getTitle())
                .description(post.getDescription())
                .postType(post.getPostType())
                .category(post.getCategory())
                .status(post.getStatus())
                .locationName(post.getLocationName())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .rewardAmount(post.getRewardAmount())
                .likes(post.getLikes())
                .liked(isLikedByCurrentUser(post))
                .saved(isSavedByCurrentUser(post))
                .commentCount(post.getCommentCount())
                .shares(post.getShares())
                .views(post.getViews())
                .anonymous(post.getAnonymous())
                .pinned(post.getPinned())

                .media(media)

                .createdAt(post.getCreatedAt())
                .build();
    }
}