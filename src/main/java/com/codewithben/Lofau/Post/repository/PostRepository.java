package com.codewithben.Lofau.Post.repository;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.enums.PostStatus;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.group.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository
        extends JpaRepository<Post, UUID>,
        JpaSpecificationExecutor<Post> {

    Page<Post> findByStatus(PostStatus status, Pageable pageable);

    Page<Post> findByDeletedFalseOrderByCreatedAtDesc(
            Pageable pageable
    );

    Page<Post> findByUserAndDeletedFalseOrderByCreatedAtDesc(
            User user,
            Pageable pageable
    );

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.group = :group
      AND p.deleted = false
    ORDER BY
        p.pinned DESC,
        p.pinnedAt DESC,
        p.createdAt DESC
""")
    Page<Post> findGroupFeed(
            @Param("group") Group group,
            Pageable pageable
    );
    Optional<Post> findByGroupAndPinnedTrueAndDeletedFalse(
            Group group
    );

}
