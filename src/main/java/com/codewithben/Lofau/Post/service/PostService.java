package com.codewithben.Lofau.Post.service;

import com.codewithben.Lofau.Post.dto.request.CreatePostRequest;
import com.codewithben.Lofau.Post.dto.response.PostResponse;

public interface PostService {

    PostResponse createPost(CreatePostRequest request);

}
