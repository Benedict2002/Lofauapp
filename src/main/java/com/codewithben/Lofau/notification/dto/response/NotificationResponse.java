package com.codewithben.Lofau.notification.dto.response;

import com.codewithben.Lofau.media.dto.response.MediaResponse;
import com.codewithben.Lofau.notification.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationResponse {

    /*
     * Notification
     */
    private UUID id;

    private NotificationType type;

    private UUID referenceId;

    private String actionUrl;

    private String message;

    private Boolean read;

    private LocalDateTime readAt;

    /*
     * Actor
     */
    private UUID actorId;

    private String actorUsername;

    private String actorFirstName;

    private String actorLastName;

    private MediaResponse actorProfileImage;

    /*
     * Preview Image
     *
     * Example:
     * - Post image
     * - Event banner
     * - Group profile
     * - Lost item image
     */
    private MediaResponse previewImage;

    /*
     * Dates
     */
    private LocalDateTime createdAt;

}