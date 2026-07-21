package com.codewithben.Lofau.Post.repository;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.Post.entity.PostView;
import com.codewithben.Lofau.User.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostViewRepository
        extends JpaRepository<PostView, UUID> {

    boolean existsByPostAndUser(
            Post post,
            User user
    );

    long countByPost(Post post);
}