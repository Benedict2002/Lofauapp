package com.codewithben.Lofau.notification.dto;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.notification.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NotificationRequest {

    private User recipient;

    private User actor;

    private NotificationType type;

    private UUID referenceId;

    private UUID ownerId;

    private OwnerType ownerType;

    private String message;
}