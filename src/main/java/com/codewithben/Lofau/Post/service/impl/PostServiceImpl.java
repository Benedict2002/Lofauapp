package com.codewithben.Lofau.Post.service.impl;

import com.codewithben.Lofau.Post.dto.request.CreatePostRequest;
import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.mapper.PostMapper;
import com.codewithben.Lofau.Post.repository.PostRepository;
import com.codewithben.Lofau.Post.service.PostService;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
   

    //String email = authentication.getName();

    @Override
    public PostResponse createPost(CreatePostRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("No authenticated user found");
        }

        String email = authentication.getName();

        System.out.println("Logged in user: " + email);

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postMapper.toEntity(request);
        post.setUser(user);

        return postMapper.toResponse(postRepository.save(post));
    }

}
