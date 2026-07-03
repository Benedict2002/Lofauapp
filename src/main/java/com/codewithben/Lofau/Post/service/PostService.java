package com.codewithben.Lofau.Post.service;

import com.codewithben.Lofau.Post.dto.request.CreatePostRequest;
import com.codewithben.Lofau.Post.dto.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    PostResponse createPost(
            CreatePostRequest request,
            List<MultipartFile> files
    ) throws IOException;

}
