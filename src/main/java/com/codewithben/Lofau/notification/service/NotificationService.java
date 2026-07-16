package com.codewithben.Lofau.notification.service;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.comment.entity.Comment;
import com.codewithben.Lofau.notification.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationService {

    void notifyPostLiked(User actor, Post post);

    void notifyPostCommented(User actor, Post post);

    void notifyCommentReplied(User actor, Comment comment);

    Page<NotificationResponse> getMyNotifications(Pageable pageable);

    Long getUnreadCount();

    void markAsRead(UUID notificationId);

    void markAllAsRead();
}