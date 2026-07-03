package com.codewithben.Lofau.Post.controller;

import com.codewithben.Lofau.Post.dto.request.CreatePostRequest;
import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PostResponse> createPost(

            @Valid
            @ModelAttribute CreatePostRequest request,

            @RequestPart(value = "files", required = false)
            List<MultipartFile> files

    ) throws IOException {

        PostResponse response = postService.createPost(request, files);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

}