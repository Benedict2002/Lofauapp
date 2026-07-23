package com.codewithben.Lofau.notification.service.impl;

import com.codewithben.Lofau.Post.entity.Post;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.comment.entity.Comment;
import com.codewithben.Lofau.notification.dto.response.NotificationResponse;
import com.codewithben.Lofau.notification.entity.Notification;
import com.codewithben.Lofau.notification.factoryN.NotificationFactory;
import com.codewithben.Lofau.notification.mapper.NotificationMapper;
import com.codewithben.Lofau.notification.repository.NotificationRepository;
import com.codewithben.Lofau.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final NotificationFactory notificationFactory;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void notifyPostLiked(User actor, Post post) {

        if (actor.getId().equals(post.getUser().getId())) {
            return;
        }

        notificationRepository.save(
                notificationFactory.createPostLiked(actor, post)
        );
    }

    @Override
    public void notifyPostCommented(User actor, Post post) {

        if (actor.getId().equals(post.getUser().getId())) {
            return;
        }

        notificationRepository.save(
                notificationFactory.createPostCommented(actor, post)
        );
    }

    @Override
    public void notifyCommentReplied(User actor, Comment comment) {

        if (actor.getId().equals(comment.getUser().getId())) {
            return;
        }

        notificationRepository.save(
                notificationFactory.createCommentReply(actor, comment)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getMyNotifications(Pageable pageable) {

        User user = getCurrentUser();

        return notificationRepository
                .findByRecipientOrderByCreatedAtDesc(user, pageable)
                .map(notificationMapper::toResponse);
    }

    @Override
    public Long getUnreadCount() {

        User user = getCurrentUser();

        return notificationRepository
                .countByRecipientAndReadFalse(user);
    }

    @Override
    public void markAsRead(UUID notificationId) {

        User user = getCurrentUser();

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getRecipient().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        if (!notification.getRead()) {
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());

            notificationRepository.save(notification);
        }
    }

    @Override
    public void markAllAsRead() {

        User user = getCurrentUser();

        notificationRepository
                .findByRecipientOrderByCreatedAtDesc(user, Pageable.unpaged())
                .forEach(notification -> {

                    if (!notification.getRead()) {

                        notification.setRead(true);
                        notification.setReadAt(LocalDateTime.now());

                        notificationRepository.save(notification);
                    }
                });
    }

}