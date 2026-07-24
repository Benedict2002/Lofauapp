package com.codewithben.Lofau.comment.service;

import com.codewithben.Lofau.comment.dto.request.CreateCommentRequest;
import com.codewithben.Lofau.comment.dto.response.CommentResponse;
import com.codewithben.Lofau.media.enums.OwnerType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    CommentResponse createComment(
            UUID ownerId,
            OwnerType ownerType,
            CreateCommentRequest request
    );

    Page<CommentResponse> getComments(
            UUID ownerId,
            OwnerType ownerType,
            int page,
            int size
    );

    CommentResponse replyToComment(
            UUID commentId,
            CreateCommentRequest request
    );

    List<CommentResponse> getReplies(
            UUID commentId
    );

}