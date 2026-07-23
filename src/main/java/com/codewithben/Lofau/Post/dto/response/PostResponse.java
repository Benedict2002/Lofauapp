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

    /*
     * Author
     */
    private UUID userId;

    private String username;

    private MediaResponse userProfileImage;

    /*
     * Post Details
     */
    private String title;

    private String description;

    private PostType postType;

    private Category category;

    private PostStatus status;

    /*
     * Group
     */
    private UUID groupId;

    private String groupName;

    /*
     * Location
     */
    private String locationName;

    private Double latitude;

    private Double longitude;

    /*
     * Reward
     */
    private BigDecimal rewardAmount;

    /*
     * Statistics
     */
    private Integer likes;

    private Integer commentCount;

    private Integer shares;

    private Integer views;

    /*
     * Current User
     */
    private Boolean liked;

    private Boolean saved;

    /*
     * Settings
     */
    private Boolean anonymous;

    private Boolean pinned;

    /*
     * Media
     */
    private MediaResponse previewImage;

    private Integer mediaCount;

    private List<MediaResponse> media;

    /*
     * Dates
     */
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}