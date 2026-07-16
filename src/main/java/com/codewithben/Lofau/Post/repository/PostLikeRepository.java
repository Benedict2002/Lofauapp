package com.codewithben.Lofau.Post.repository;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.entity.PostLike;
import com.codewithben.Lofau.User.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {

    boolean existsByPostAndUser(
            Post post,
            User user
    );

    Optional<PostLike> findByPostAndUser(
            Post post,
            User user
    );

    long countByPost(
            Post post
    );

    void deleteByPostAndUser(
            Post post,
            User user
    );

}