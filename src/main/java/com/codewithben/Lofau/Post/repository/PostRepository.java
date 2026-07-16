package com.codewithben.Lofau.Post.repository;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.enums.PostStatus;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.group.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostRepository
        extends JpaRepository<Post, UUID>,
        JpaSpecificationExecutor<Post> {

    Page<Post> findByStatus(PostStatus status, Pageable pageable);

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Post> findByUserOrderByCreatedAtDesc(
            User user,
            Pageable pageable
    );

    Page<Post> findByGroupOrderByCreatedAtDesc(
            Group group,
            Pageable pageable
    );

}
