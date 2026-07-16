package com.codewithben.Lofau.search.controller;

import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.enums.Category;
import com.codewithben.Lofau.Post.enums.PostType;
import com.codewithben.Lofau.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponse>> searchPosts(

            @RequestParam(required = false)
            String keyword,

            @RequestParam(required = false)
            PostType postType,

            @RequestParam(required = false)
            Category category,

            @RequestParam(required = false)
            String location,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size

    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(

                searchService.searchPosts(
                        keyword,
                        postType,
                        category,
                        location,
                        pageable
                )

        );
    }
}