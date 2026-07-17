package com.codewithben.Lofau.Post.repository;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.entity.SavedPost;
import com.codewithben.Lofau.User.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SavedPostRepository
        extends JpaRepository<SavedPost, UUID> {

    boolean existsByUserAndPost(
            User user,
            Post post
    );

    Optional<SavedPost> findByUserAndPost(
            User user,
            Post post
    );

    Page<SavedPost> findByUserOrderByCreatedAtDesc(
            User user,
            Pageable pageable
    );
}