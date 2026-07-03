package com.codewithben.Lofau.Post.dto.response;

import com.codewithben.Lofau.Post.enums.Category;
import com.codewithben.Lofau.Post.enums.PostStatus;
import com.codewithben.Lofau.Post.enums.PostType;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PostResponse {

    private UUID id;

    private Long userId;

    private String username;

    private String title;

    private String description;

    private PostType postType;

    private Category category;

    private PostStatus status;

    private String locationName;

    private Double latitude;

    private Double longitude;

    private BigDecimal rewardAmount;

    private Integer likes;

    private Integer commentCount;

    private Integer shares;

    private Integer views;

    private Boolean anonymous;


    private List<MediaResponse> media;

    private LocalDateTime createdAt;

}