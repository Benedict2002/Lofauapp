package com.codewithben.Lofau.Post.service;

import com.codewithben.Lofau.Post.dto.request.CreatePostRequest;
import com.codewithben.Lofau.Post.dto.request.UpdatePostRequest;
import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.enums.Category;
import com.codewithben.Lofau.Post.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PostService {

    PostResponse createPost(
            CreatePostRequest request,
            List<MultipartFile> files
    ) throws IOException;

    PostResponse likePost(UUID postId);

    PostResponse unlikePost(UUID postId);
    Page<PostResponse> getFeed(Pageable pageable);

    PostResponse getPostById(UUID postId);

    Page<PostResponse> getPostsByUser(
            UUID userId,
            Pageable pageable
    );

    Page<PostResponse> getPostsByGroup(
            UUID groupId,
            Pageable pageable
    );
    PostResponse savePost(UUID postId);

    PostResponse unsavePost(UUID postId);

    Page<PostResponse> getSavedPosts(Pageable pageable);
    PostResponse sharePost(UUID postId);
    public PostResponse updatePost(
            UUID postId,
            UpdatePostRequest request,
            List<MultipartFile> files
    ) throws IOException;

    void deletePost(UUID postId);



}
