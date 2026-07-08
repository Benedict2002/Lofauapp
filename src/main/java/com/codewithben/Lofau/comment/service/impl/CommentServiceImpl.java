package com.codewithben.Lofau.comment.service.impl;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.repository.PostRepository;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.comment.dto.request.CreateCommentRequest;
import com.codewithben.Lofau.comment.dto.response.CommentResponse;
import com.codewithben.Lofau.comment.entity.Comment;
import com.codewithben.Lofau.comment.mapper.CommentMapper;
import com.codewithben.Lofau.comment.repository.CommentRepository;
import com.codewithben.Lofau.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(
            UUID postId,
            CreateCommentRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .build();

        comment = commentRepository.save(comment);

        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        return commentMapper.toResponse(comment);
    }

    @Override
    public Page<CommentResponse> getComments(
            UUID postId,
            int page,
            int size
    ) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return commentRepository
                .findByPostAndParentIsNullOrderByCreatedAtDesc(
                        post,
                        PageRequest.of(page, size)
                )
                .map(commentMapper::toResponse);
    }
}