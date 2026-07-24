package com.codewithben.Lofau.event.dto.response;

import com.codewithben.Lofau.event.enums.AttendanceStatus;
import com.codewithben.Lofau.event.enums.EventStatus;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class EventResponse {

    // Event
    private UUID id;
    private String title;
    private String description;

    // Location
    private String location;
    private Double latitude;
    private Double longitude;

    // Dates
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Capacity
    private Integer capacity;

    // Status
    private EventStatus status;
    private Boolean active;
    private Boolean cancelled;
    private Boolean verified;
    private Boolean featured;

    // Statistics
    private Integer goingCount;
    private Integer interestedCount;
    private Integer viewCount;
    private Integer shareCount;

    // Creator
    private UUID creatorId;
    private String creatorUsername;
    private MediaResponse creatorProfileImage;

    // Group
    private UUID groupId;
    private String groupName;

    // Media
    private MediaResponse coverImage;
    private MediaResponse previewImage;
    private Integer mediaCount;
    private List<MediaResponse> gallery;

    // Current user
    private Boolean going;
    private Boolean interested;
    private AttendanceStatus attendanceStatus;

    private Integer commentCount;

    private Integer likeCount;

    private Boolean saved;

    private Boolean liked;

    private Boolean commentsEnabled;

    @Builder.Default
    private List<EventAttendeeResponse> attendeesPreview = new ArrayList<>();

    // Dates
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}