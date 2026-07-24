package com.codewithben.Lofau.comment.controller;

import com.codewithben.Lofau.comment.dto.request.CreateCommentRequest;
import com.codewithben.Lofau.comment.dto.response.CommentResponse;
import com.codewithben.Lofau.comment.service.CommentService;
import com.codewithben.Lofau.media.enums.OwnerType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{ownerType}/{ownerId}")
    public ResponseEntity<CommentResponse> createComment(

            @PathVariable OwnerType ownerType,

            @PathVariable UUID ownerId,

            @Valid
            @RequestBody CreateCommentRequest request

    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        commentService.createComment(
                                ownerId,
                                ownerType,
                                request
                        )
                );
    }

    @GetMapping("/{ownerType}/{ownerId}")
    public ResponseEntity<Page<CommentResponse>> getComments(

            @PathVariable OwnerType ownerType,

            @PathVariable UUID ownerId,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size

    ) {

        return ResponseEntity.ok(

                commentService.getComments(
                        ownerId,
                        ownerType,
                        page,
                        size
                )

        );
    }

    @PostMapping("/{commentId}/reply")
    public ResponseEntity<CommentResponse> replyToComment(

            @PathVariable UUID commentId,

            @Valid
            @RequestBody CreateCommentRequest request

    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        commentService.replyToComment(
                                commentId,
                                request
                        )
                );
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<CommentResponse>> getReplies(

            @PathVariable UUID commentId

    ) {

        return ResponseEntity.ok(
                commentService.getReplies(commentId)
        );
    }

}