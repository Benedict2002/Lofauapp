package com.codewithben.Lofau.comment.service;

import com.codewithben.Lofau.comment.dto.request.CreateCommentRequest;
import com.codewithben.Lofau.comment.dto.response.CommentResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    CommentResponse createComment(
            UUID postId,
            CreateCommentRequest request
    );

    Page<CommentResponse> getComments(
            UUID postId,
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