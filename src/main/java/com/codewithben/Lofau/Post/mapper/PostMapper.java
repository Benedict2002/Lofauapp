package com.codewithben.Lofau.Post.mapper;

import com.codewithben.Lofau.Post.dto.request.CreatePostRequest;
import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final MediaService mediaService;

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

        return PostResponse.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .username(post.getUser().getUsername())
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
                .commentCount(post.getCommentCount())
                .shares(post.getShares())
                .views(post.getViews())
                .anonymous(post.getAnonymous())
                .media(
                        mediaService.getMedia(
                                post.getId(),
                                OwnerType.POST
                        )
                )
                .createdAt(post.getCreatedAt())
                .build();
    }
}