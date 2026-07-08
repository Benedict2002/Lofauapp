package com.codewithben.Lofau.comment.controller;

import com.codewithben.Lofau.comment.dto.request.CreateCommentRequest;
import com.codewithben.Lofau.comment.dto.response.CommentResponse;
import com.codewithben.Lofau.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(

            @PathVariable UUID postId,

            @Valid
            @RequestBody CreateCommentRequest request

    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(postId, request));

    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Page<CommentResponse>> getComments(

            @PathVariable UUID postId,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size

    ) {

        return ResponseEntity.ok(

                commentService.getComments(
                        postId,
                        page,
                        size
                )

        );

    }

}