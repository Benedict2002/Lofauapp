package com.codewithben.Lofau.event.mapper;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.comment.repository.CommentRepository;
import com.codewithben.Lofau.event.dto.request.CreateEventRequest;
import com.codewithben.Lofau.event.dto.request.UpdateEventRequest;
import com.codewithben.Lofau.event.dto.response.EventAttendeeResponse;
import com.codewithben.Lofau.event.dto.response.EventResponse;
import com.codewithben.Lofau.event.entity.Event;
import com.codewithben.Lofau.event.entity.EventAttendance;
import com.codewithben.Lofau.event.enums.AttendanceStatus;
import com.codewithben.Lofau.event.repository.EventAttendanceRepository;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final MediaService mediaService;
    private final EventAttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    public Event toEntity(CreateEventRequest request) {

        if (request == null) {
            return null;
        }

        return Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .capacity(request.getCapacity())
                .build();
    }

    public void updateEntity(
            Event event,
            UpdateEventRequest request
    ) {

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }

        if (request.getLatitude() != null) {
            event.setLatitude(request.getLatitude());
        }

        if (request.getLongitude() != null) {
            event.setLongitude(request.getLongitude());
        }

        if (request.getStartDate() != null) {
            event.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            event.setEndDate(request.getEndDate());
        }

        if (request.getCapacity() != null) {
            event.setCapacity(request.getCapacity());
        }

        if (request.getActive() != null) {
            event.setActive(request.getActive());
        }

        if (request.getCancelled() != null) {
            event.setCancelled(request.getCancelled());
        }

        if (request.getFeatured() != null) {
            event.setFeatured(request.getFeatured());
        }

        if (request.getCommentsEnabled() != null) {
            event.setCommentsEnabled(request.getCommentsEnabled());
        }
    }

    public EventResponse toResponse(Event event) {

        List<EventAttendeeResponse> attendees =
                attendanceRepository
                        .findTop3ByEventAndStatusOrderByIdAsc(
                                event,
                                AttendanceStatus.GOING
                        )
                        .stream()
                        .map(attendance -> EventAttendeeResponse.builder()
                                .id(attendance.getUser().getId())
                                .username(attendance.getUser().getDisplayUsername())
                                .profileImage(
                                        mediaService.getProfile(
                                                attendance.getUser().getId(),
                                                OwnerType.USER
                                        )
                                )
                                .build())
                        .toList();

        List<MediaResponse> gallery =
                mediaService.getGallery(
                        event.getId(),
                        OwnerType.EVENT
                );

        MediaResponse coverImage =
                mediaService.getCover(
                        event.getId(),
                        OwnerType.EVENT
                );

        MediaResponse previewImage =
                !gallery.isEmpty()
                        ? gallery.get(0)
                        : coverImage;

        long commentCount =
                commentRepository.countByOwnerIdAndOwnerType(
                        event.getId(),
                        OwnerType.EVENT
                );

        boolean going = false;
        boolean interested = false;
        AttendanceStatus attendanceStatus = null;

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {

            Optional<User> user =
                    userRepository.findByEmail(authentication.getName());

            if (user.isPresent()) {

                Optional<EventAttendance> attendance =
                        attendanceRepository.findByEventAndUser(
                                event,
                                user.get()
                        );

                if (attendance.isPresent()) {

                    attendanceStatus = attendance.get().getStatus();

                    going =
                            attendanceStatus == AttendanceStatus.GOING;

                    interested =
                            attendanceStatus == AttendanceStatus.INTERESTED;
                }
            }
        }

        return EventResponse.builder()

                .id(event.getId())

                .title(event.getTitle())
                .description(event.getDescription())

                .location(event.getLocation())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())

                .startDate(event.getStartDate())
                .endDate(event.getEndDate())

                .capacity(event.getCapacity())

                .status(event.getStatus())
                .active(event.getActive())
                .cancelled(event.getCancelled())
                .verified(event.getVerified())
                .featured(event.getFeatured())

                .goingCount(event.getGoingCount())
                .interestedCount(event.getInterestedCount())
                .commentCount(event.getCommentCount())
                .likeCount(event.getLikeCount())
                .viewCount(event.getViewCount())
                .shareCount(event.getShareCount())

                .creatorId(event.getCreatedBy().getId())
                .creatorUsername(event.getCreatedBy().getDisplayUsername())
                .creatorProfileImage(
                        mediaService.getProfile(
                                event.getCreatedBy().getId(),
                                OwnerType.USER
                        )
                )

                .groupId(event.getGroup().getId())
                .groupName(event.getGroup().getName())

                .coverImage(coverImage)
                .previewImage(previewImage)
                .mediaCount(gallery.size())
                .gallery(gallery)

                .attendeesPreview(attendees)

                .liked(false)
                .saved(false)

                .going(going)
                .interested(interested)
                .attendanceStatus(attendanceStatus)
                .commentsEnabled(event.getCommentsEnabled())

                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())

                .build();
    }
}