package com.codewithben.Lofau.event.service.impl;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.event.dto.request.CreateEventRequest;
import com.codewithben.Lofau.event.dto.request.UpdateEventRequest;
import com.codewithben.Lofau.event.dto.response.EventResponse;
import com.codewithben.Lofau.event.entity.Event;
import com.codewithben.Lofau.event.entity.EventAttendance;
import com.codewithben.Lofau.event.enums.AttendanceStatus;
import com.codewithben.Lofau.event.mapper.EventMapper;
import com.codewithben.Lofau.event.repository.EventAttendanceRepository;
import com.codewithben.Lofau.event.repository.EventRepository;
import com.codewithben.Lofau.event.service.EventService;
import com.codewithben.Lofau.group.entity.Group;
import com.codewithben.Lofau.group.repository.GroupRepository;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final GroupRepository groupRepository;
    private final EventAttendanceRepository attendanceRepository;
    private final EventMapper eventMapper;
    private final MediaService mediaService;
    private final UserRepository userRepository;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Event getEvent(UUID id) {

        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event not found"));
    }

    private Group getGroup(UUID id) {

        return groupRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Group not found"));
    }

    @Override
    public EventResponse createEvent(
            CreateEventRequest request,
            MultipartFile coverImage,
            List<MultipartFile> gallery
    ) throws IOException {

        User currentUser = getCurrentUser();

        Group group = getGroup(request.getGroupId());

        Event event = eventMapper.toEntity(request);

        event.setCreatedBy(currentUser);
        event.setGroup(group);

        event = eventRepository.save(event);

        if (coverImage != null && !coverImage.isEmpty()) {

            mediaService.uploadCover(
                    event.getId(),
                    coverImage,
                    OwnerType.EVENT
            );
        }

        if (gallery != null && !gallery.isEmpty()) {

            mediaService.uploadGallery(
                    event.getId(),
                    gallery,
                    OwnerType.EVENT
            );
        }

        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse updateEvent(
            UUID eventId,
            UpdateEventRequest request,
            MultipartFile coverImage,
            List<MultipartFile> gallery
    ) throws IOException {


        Event event = getEvent(eventId);
        validateOwnership(event);


        eventMapper.updateEntity(event, request);

        event = eventRepository.save(event);

        if (coverImage != null && !coverImage.isEmpty()) {

            mediaService.uploadCover(
                    event.getId(),
                    coverImage,
                    OwnerType.EVENT
            );
        }

        if (gallery != null && !gallery.isEmpty()) {

            mediaService.uploadGallery(
                    event.getId(),
                    gallery,
                    OwnerType.EVENT
            );
        }


        return eventMapper.toResponse(event);
    }

    @Override
    public void deleteEvent(UUID eventId) {

        Event event = getEvent(eventId);
        validateOwnership(event);

        event.setActive(false);

        eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getEventById(UUID eventId) {

        return eventMapper.toResponse(
                getEvent(eventId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getAllEvents(
            Pageable pageable
    ) {

        return eventRepository
                .findByActiveTrue(pageable)
                .map(eventMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getGroupEvents(
            UUID groupId,
            Pageable pageable
    ) {

        Group group = getGroup(groupId);

        return eventRepository
                .findByGroup(group, pageable)
                .map(eventMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getMyEvents(
            Pageable pageable
    ) {

        User currentUser = getCurrentUser();

        return eventRepository
                .findByCreatedBy(currentUser, pageable)
                .map(eventMapper::toResponse);
    }

    @Override
    public EventResponse markGoing(UUID eventId) {

        User currentUser = getCurrentUser();

        Event event = getEvent(eventId);
        if (event.getCapacity() != null &&
                event.getGoingCount() >= event.getCapacity()) {

            throw new RuntimeException("Event is full.");
        }

        EventAttendance attendance =
                attendanceRepository
                        .findByEventAndUser(
                                event,
                                currentUser
                        )
                        .orElse(
                                EventAttendance.builder()
                                        .event(event)
                                        .user(currentUser)
                                        .build()
                        );

        attendance.setStatus(
                AttendanceStatus.GOING
        );

        attendanceRepository.save(attendance);

        refreshAttendanceCounts(event);

        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse markInterested(UUID eventId) {

        User currentUser = getCurrentUser();

        Event event = getEvent(eventId);

        EventAttendance attendance =
                attendanceRepository
                        .findByEventAndUser(
                                event,
                                currentUser
                        )
                        .orElse(
                                EventAttendance.builder()
                                        .event(event)
                                        .user(currentUser)
                                        .build()
                        );

        attendance.setStatus(
                AttendanceStatus.INTERESTED
        );

        attendanceRepository.save(attendance);

        refreshAttendanceCounts(event);

        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse removeAttendance(UUID eventId) {

        User currentUser = getCurrentUser();

        Event event = getEvent(eventId);

        attendanceRepository.deleteByEventAndUser(
                event,
                currentUser
        );

        refreshAttendanceCounts(event);

        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse incrementShareCount(
            UUID eventId
    ) {

        Event event = getEvent(eventId);

        event.setShareCount(
                event.getShareCount() + 1
        );

        eventRepository.save(event);

        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse incrementViewCount(
            UUID eventId
    ) {

        Event event = getEvent(eventId);

        event.setViewCount(
                event.getViewCount() + 1
        );

        eventRepository.save(event);

        return eventMapper.toResponse(event);
    }
    private void refreshAttendanceCounts(Event event) {

        event.setGoingCount(
                (int) attendanceRepository.countByEventAndStatus(
                        event,
                        AttendanceStatus.GOING
                )
        );

        event.setInterestedCount(
                (int) attendanceRepository.countByEventAndStatus(
                        event,
                        AttendanceStatus.INTERESTED
                )
        );

        eventRepository.save(event);
    }





    private void validateOwnership(Event event) {

        User currentUser = getCurrentUser();

        if (!event.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to modify this event.");
        }
    }


}