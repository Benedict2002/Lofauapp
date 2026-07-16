package com.codewithben.Lofau.notification.repository;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<Notification, UUID> {

    Page<Notification> findByRecipientOrderByCreatedAtDesc(
            User recipient,
            Pageable pageable
    );

    Long countByRecipientAndReadFalse(User recipient);
}