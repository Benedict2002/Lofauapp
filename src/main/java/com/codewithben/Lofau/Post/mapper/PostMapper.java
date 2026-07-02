package com.codewithben.Lofau.Post.mapper;

import com.codewithben.Lofau.Post.dto.request.CreatePostRequest;
import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.entity.PostMedia;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    public Post toEntity(CreatePostRequest request) {

        return Post.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .postType(request.getPostType())
                .category(request.getCategory())
                .locationName(request.getLocationName())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .rewardAmount(request.getRewardAmount())
                .anonymous(Boolean.TRUE.equals(request.getAnonymous()))
                .build();
    }

    public PostResponse toResponse(Post post) {

        return PostResponse.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .username(post.getAnonymous()
                        ? "Anonymous"
                        : post.getUser().getUsername())
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
                .mediaUrls(
                        post.getMedia() == null
                                ? Collections.emptyList()
                                : post.getMedia()
                                  .stream()
                                  .map(PostMedia::getMediaUrl)
                                  .collect(Collectors.toList())
                )
                .createdAt(post.getCreatedAt())
                .build();
    }

}
