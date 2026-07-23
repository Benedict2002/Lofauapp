package com.codewithben.Lofau.comment.dto.response;

import com.codewithben.Lofau.media.dto.response.MediaResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CommentResponse {

    private UUID id;

    private UUID userId;

    private String username;

    private String firstName;

    private String lastName;

    private MediaResponse profileImage;

    private String content;

    private Boolean edited;

    private Integer likeCount;

    private Integer replyCount;

    private LocalDateTime createdAt;

    private List<CommentResponse> replies;

}