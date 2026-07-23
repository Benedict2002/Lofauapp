package com.codewithben.Lofau.comment.mapper;

import com.codewithben.Lofau.comment.dto.response.CommentResponse;
import com.codewithben.Lofau.comment.entity.Comment;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final MediaService mediaService;

    public CommentResponse toResponse(Comment comment) {

        if (comment == null) {
            return null;
        }

        return CommentResponse.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .username(comment.getUser().getDisplayUsername())
                .firstName(comment.getUser().getFirstName())
                .lastName(comment.getUser().getLastName())
                .profileImage(
                        mediaService.getProfile(
                                comment.getUser().getId(),
                                OwnerType.USER
                        )
                )
                .content(comment.getContent())
                .edited(comment.getEdited())
                .likeCount(comment.getLikeCount())
                .replyCount(comment.getReplyCount())
                .createdAt(comment.getCreatedAt())
                .replies(
                        comment.getReplies() == null
                                ? Collections.emptyList()
                                : comment.getReplies()
                                  .stream()
                                  .map(this::toResponse)
                                  .collect(Collectors.toList())
                )
                .build();
    }
}