package com.codewithben.Lofau.comment.repository;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.comment.entity.Comment;
import com.codewithben.Lofau.media.enums.OwnerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository
        extends JpaRepository<Comment, UUID> {

    /**
     * Top-level comments for any owner
     */
    Page<Comment> findByOwnerIdAndOwnerTypeAndParentIsNullOrderByCreatedAtDesc(
            UUID ownerId,
            OwnerType ownerType,
            Pageable pageable
    );

    /**
     * Replies
     */
    List<Comment> findByParentOrderByCreatedAtAsc(
            Comment parent
    );

    /**
     * Comments made by a user
     */
    List<Comment> findByUser(
            User user
    );

    /**
     * Number of comments for an owner
     */
    long countByOwnerIdAndOwnerType(
            UUID ownerId,
            OwnerType ownerType
    );

    /**
     * Number of replies
     */
    long countByParent(
            Comment parent
    );

    List<Comment> findByOwnerIdAndOwnerType(
            UUID ownerId,
            OwnerType ownerType
    );
}