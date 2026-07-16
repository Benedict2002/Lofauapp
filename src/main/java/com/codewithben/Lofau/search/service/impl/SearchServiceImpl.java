package com.codewithben.Lofau.search.service.impl;

import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.Post.enums.Category;
import com.codewithben.Lofau.Post.enums.PostType;
import com.codewithben.Lofau.Post.mapper.PostMapper;
import com.codewithben.Lofau.Post.repository.PostRepository;
import com.codewithben.Lofau.search.service.SearchService;
import com.codewithben.Lofau.search.specification.PostSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public Page<PostResponse> searchPosts(
            String keyword,
            PostType postType,
            Category category,
            String location,
            Pageable pageable
    ) {

        return postRepository.findAll(
                PostSpecification.search(
                        keyword,
                        postType,
                        category,
                        location
                ),
                pageable
        ).map(postMapper::toResponse);
    }
}