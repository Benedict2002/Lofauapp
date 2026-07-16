package com.codewithben.Lofau.Post.controller;

import com.codewithben.Lofau.Post.dto.request.CreatePostRequest;
import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.enums.Category;
import com.codewithben.Lofau.Post.enums.PostType;
import com.codewithben.Lofau.Post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> createPost(

            @RequestParam(required = false) UUID groupId,

            @RequestParam(required = false) String title,

            @RequestParam String description,

            @RequestParam PostType postType,

            @RequestParam Category category,

            @RequestParam(required = false) String locationName,

            @RequestParam(required = false) Double latitude,

            @RequestParam(required = false) Double longitude,

            @RequestParam(required = false) BigDecimal rewardAmount,

            @RequestParam(defaultValue = "false") Boolean anonymous,

            @RequestPart(required = false) List<MultipartFile> files

    ) throws IOException {

        CreatePostRequest request = new CreatePostRequest();

        request.setGroupId(groupId);
        request.setTitle(title);
        request.setDescription(description);
        request.setPostType(postType);
        request.setCategory(category);
        request.setLocationName(locationName);
        request.setLatitude(latitude);
        request.setLongitude(longitude);
        request.setRewardAmount(rewardAmount);
        request.setAnonymous(anonymous);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createPost(request, files));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostResponse> likePost(
            @PathVariable UUID postId
    ) {

        return ResponseEntity.ok(
                postService.likePost(postId)
        );
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<PostResponse> unlikePost(
            @PathVariable UUID postId
    ) {

        return ResponseEntity.ok(
                postService.unlikePost(postId)
        );

    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getFeed(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                postService.getFeed(pageable)
        );
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable UUID postId
    ) {

        return ResponseEntity.ok(
                postService.getPostById(postId)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostResponse>> getUserPosts(
            @PathVariable Long userId,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                postService.getPostsByUser(
                        userId,
                        pageable
                )
        );
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<Page<PostResponse>> getGroupPosts(
            @PathVariable UUID groupId,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                postService.getPostsByGroup(
                        groupId,
                        pageable
                )
        );
    }


}