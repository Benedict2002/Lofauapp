package com.codewithben.Lofau.notification.mapper;

import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.service.MediaService;
import com.codewithben.Lofau.notification.dto.response.NotificationResponse;
import com.codewithben.Lofau.notification.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMapper {

    private final MediaService mediaService;

    public NotificationResponse toResponse(Notification notification) {

        if (notification == null) {
            return null;
        }

        return NotificationResponse.builder()

                .id(notification.getId())

                /*
                 * Actor
                 */
                .actorId(notification.getActor().getId())
                .previewImage(

                        notification.getPreviewOwnerId() == null
                                ? null
                                : mediaService.getGallery(
                                notification.getPreviewOwnerId(),
                                notification.getPreviewOwnerType()
                        ).stream()
                                  .findFirst()
                                  .orElse(null)

                )
                .actorUsername(notification.getActor().getDisplayUsername())
                .actorFirstName(notification.getActor().getFirstName())
                .actorLastName(notification.getActor().getLastName())
                .actorProfileImage(
                        mediaService.getProfile(
                                notification.getActor().getId(),
                                OwnerType.USER
                        )
                )

                /*
                 * Notification
                 */
                .type(notification.getType())
                .referenceId(notification.getReferenceId())
                .actionUrl(notification.getActionUrl())
                .message(notification.getMessage())
                .read(notification.getRead())
                .readAt(notification.getReadAt())

                /*
                 * Dates
                 */
                .createdAt(notification.getCreatedAt())

                .build();
    }
}