package com.codewithben.Lofau.Post.service.impl;

import com.codewithben.Lofau.Post.dto.request.CreatePostRequest;
import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.mapper.PostMapper;
import com.codewithben.Lofau.Post.repository.PostRepository;
import com.codewithben.Lofau.Post.service.PostService;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final MediaService mediaService;

    @Override
    public PostResponse createPost(
            CreatePostRequest request,
            List<MultipartFile> files
    ) throws IOException {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postMapper.toEntity(request);

        post.setUser(user);

        Post savedPost = postRepository.save(post);

        // Upload images after the post has been saved
        mediaService.saveMedia(
                savedPost.getId(),
                files,
                OwnerType.POST
        );

        // Return the complete post with media
        return postMapper.toResponse(savedPost);
    }

}