package com.codewithben.Lofau.notification.factoryN;

import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.notification.dto.NotificationRequest;
import com.codewithben.Lofau.notification.entity.Notification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationFactory {

    public Notification create(NotificationRequest request) {

        return Notification.builder()
                .recipient(request.getRecipient())
                .actor(request.getActor())
                .type(request.getType())

                .referenceId(request.getReferenceId())

                .previewOwnerId(request.getOwnerId())
                .previewOwnerType(request.getOwnerType())

                .actionUrl(
                        buildActionUrl(
                                request.getOwnerType(),
                                request.getOwnerId()
                        )
                )

                .message(request.getMessage())

                .build();
    }

    private String buildActionUrl(
            OwnerType ownerType,
            UUID ownerId
    ) {

        return switch (ownerType) {

            case POST -> "/posts/" + ownerId;

            case EVENT -> "/events/" + ownerId;

            case GROUP -> "/groups/" + ownerId;

            case USER -> "/users/" + ownerId;

            default -> "/";
        };
    }
}