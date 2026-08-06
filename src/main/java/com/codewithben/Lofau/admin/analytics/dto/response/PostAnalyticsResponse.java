package com.codewithben.Lofau.admin.analytics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostAnalyticsResponse {

    /**
     * Total posts.
     */
    private Long totalPosts;

    /**
     * Lost item posts.
     */
    private Long lostPosts;
    private  Long activePosts;

    /**
     * Found item posts.
     */
    private Long foundPosts;

    /**
     * Approved posts.
     */
    private Long approvedPosts;

    /**
     * Deleted posts.
     */
    private Long deletedPosts;

    /**
     * Pinned posts.
     */
    private Long pinnedPosts;

    private Long pendingPosts;

    private Long reportedPosts;
}