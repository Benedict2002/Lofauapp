package com.codewithben.Lofau.notification.dto.response;

import com.codewithben.Lofau.notification.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationResponse {

    private UUID id;

    private Long actorId;

    private String actorUsername;

    private String actorProfileImage;

    private NotificationType type;

    private UUID referenceId;

    private String message;

    private Boolean read;

    private LocalDateTime createdAt;
}