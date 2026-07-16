package com.codewithben.Lofau.notification.mapper;

import com.codewithben.Lofau.notification.dto.response.NotificationResponse;
import com.codewithben.Lofau.notification.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {

        if (notification == null) {
            return null;
        }

        return NotificationResponse.builder()
                .id(notification.getId())

                .actorId(notification.getActor().getId())
                .actorUsername(notification.getActor().getUsername())
                .actorProfileImage(notification.getActor().getProfilePictureUrl())

                .type(notification.getType())
                .referenceId(notification.getReferenceId())
                .message(notification.getMessage())
                .read(notification.getRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}