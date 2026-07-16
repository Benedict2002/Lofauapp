package com.codewithben.Lofau.search.service;

import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.enums.Category;
import com.codewithben.Lofau.Post.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {

    Page<PostResponse> searchPosts(
            String keyword,
            PostType postType,
            Category category,
            String location,
            Pageable pageable
    );

}