package com.codewithben.Lofau.event.service;

import com.codewithben.Lofau.event.dto.request.CreateEventRequest;
import com.codewithben.Lofau.event.dto.request.UpdateEventRequest;
import com.codewithben.Lofau.event.dto.response.EventResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface EventService {

    EventResponse createEvent(
            CreateEventRequest request,
            MultipartFile coverImage,
            List<MultipartFile> gallery
    ) throws IOException;

    EventResponse updateEvent(
            UUID eventId,
            UpdateEventRequest request,
            MultipartFile coverImage,
            List<MultipartFile> gallery
    ) throws IOException;

    void deleteEvent(
            UUID eventId
    );

    EventResponse getEventById(
            UUID eventId
    );

    Page<EventResponse> getAllEvents(
            Pageable pageable
    );

    Page<EventResponse> getGroupEvents(
            UUID groupId,
            Pageable pageable
    );

    Page<EventResponse> getMyEvents(
            Pageable pageable
    );

    EventResponse markGoing(
            UUID eventId
    );

    EventResponse markInterested(
            UUID eventId
    );

    EventResponse removeAttendance(
            UUID eventId
    );

    EventResponse incrementShareCount(
            UUID eventId
    );

    EventResponse incrementViewCount(
            UUID eventId
    );
}