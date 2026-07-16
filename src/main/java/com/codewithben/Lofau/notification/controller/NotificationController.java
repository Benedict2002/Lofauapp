package com.codewithben.Lofau.notification.controller;

import com.codewithben.Lofau.notification.dto.response.NotificationResponse;
import com.codewithben.Lofau.notification.dto.response.UnreadCountResponse;
import com.codewithben.Lofau.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public Page<NotificationResponse> getMyNotifications(
            Pageable pageable
    ) {

        return notificationService.getMyNotifications(pageable);
    }

    @GetMapping("/unread-count")
    public UnreadCountResponse getUnreadCount() {

        return new UnreadCountResponse(
                notificationService.getUnreadCount()
        );
    }

    @PatchMapping("/{notificationId}/read")
    public void markAsRead(
            @PathVariable UUID notificationId
    ) {

        notificationService.markAsRead(notificationId);
    }

    @PatchMapping("/read-all")
    public void markAllAsRead() {

        notificationService.markAllAsRead();
    }
}