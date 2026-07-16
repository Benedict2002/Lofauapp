package com.codewithben.Lofau.comment.repository;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    // Top-level comments
    Page<Comment> findByPostAndParentIsNullOrderByCreatedAtDesc(
            Post post,
            Pageable pageable
    );

    // Replies
    List<Comment> findByParentOrderByCreatedAtAsc(
            Comment parent
    );

    List<Comment> findByUser(
            User user
    );

    long countByPost(
            Post post
    );



    long countByParent(
            Comment parent
    );

}