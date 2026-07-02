package com.codewithben.Lofau.Post.repository;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    Page<Post> findByStatus(PostStatus status, Pageable pageable);

}
