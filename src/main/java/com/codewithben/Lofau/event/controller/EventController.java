package com.codewithben.Lofau.event.controller;

import com.codewithben.Lofau.Auth.dto.response.ApiResponse;
import com.codewithben.Lofau.event.dto.request.CreateEventRequest;
import com.codewithben.Lofau.event.dto.request.UpdateEventRequest;
import com.codewithben.Lofau.event.dto.response.EventResponse;
import com.codewithben.Lofau.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventResponse> createEvent(

            @RequestParam String title,

            @RequestParam(required = false)
            String description,

            @RequestParam String location,

            @RequestParam(required = false)
            Double latitude,

            @RequestParam(required = false)
            Double longitude,

            @RequestParam LocalDateTime startDate,

            @RequestParam(required = false)
            LocalDateTime endDate,

            @RequestParam(required = false)
            Integer capacity,

            @RequestParam UUID groupId,

            @RequestPart(required = false)
            MultipartFile coverImage,

            @RequestPart(required = false)
            List<MultipartFile> gallery

    ) throws IOException {

        CreateEventRequest request = new CreateEventRequest();

        request.setTitle(title);
        request.setDescription(description);
        request.setLocation(location);
        request.setLatitude(latitude);
        request.setLongitude(longitude);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setCapacity(capacity);
        request.setGroupId(groupId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        eventService.createEvent(
                                request,
                                coverImage,
                                gallery
                        )
                );
    }

    @PutMapping(
            value = "/{eventId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<EventResponse> updateEvent(

            @PathVariable UUID eventId,

            @ModelAttribute UpdateEventRequest request,

            @RequestPart(required = false)
            MultipartFile coverImage,

            @RequestPart(required = false)
            List<MultipartFile> gallery

    ) throws IOException {

        return ResponseEntity.ok(
                eventService.updateEvent(
                        eventId,
                        request,
                        coverImage,
                        gallery
                )
        );
    }

    @DeleteMapping("/{eventId}")
    public ApiResponse<Void> deleteEvent(
            @PathVariable UUID eventId
    ) {

        eventService.deleteEvent(eventId);

        return ApiResponse.success(
                "Event deleted successfully.",
                null
        );
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(
            @PathVariable UUID eventId
    ) {

        return ResponseEntity.ok(
                eventService.getEventById(eventId)
        );
    }

    @GetMapping
    public ResponseEntity<Page<EventResponse>> getEvents(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                eventService.getAllEvents(pageable)
        );
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<Page<EventResponse>> getGroupEvents(

            @PathVariable UUID groupId,

            Pageable pageable

    ) {

        return ResponseEntity.ok(
                eventService.getGroupEvents(
                        groupId,
                        pageable
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<Page<EventResponse>> getMyEvents(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                eventService.getMyEvents(pageable)
        );
    }

    @PostMapping("/{eventId}/going")
    public ResponseEntity<EventResponse> markGoing(
            @PathVariable UUID eventId
    ) {

        return ResponseEntity.ok(
                eventService.markGoing(eventId)
        );
    }

    @PostMapping("/{eventId}/interested")
    public ResponseEntity<EventResponse> markInterested(
            @PathVariable UUID eventId
    ) {

        return ResponseEntity.ok(
                eventService.markInterested(eventId)
        );
    }

    @DeleteMapping("/{eventId}/attendance")
    public ResponseEntity<EventResponse> removeAttendance(
            @PathVariable UUID eventId
    ) {

        return ResponseEntity.ok(
                eventService.removeAttendance(eventId)
        );
    }

    @PatchMapping("/{eventId}/view")
    public ResponseEntity<EventResponse> viewEvent(
            @PathVariable UUID eventId
    ) {

        return ResponseEntity.ok(
                eventService.incrementViewCount(eventId)
        );
    }

    @PatchMapping("/{eventId}/share")
    public ResponseEntity<EventResponse> shareEvent(
            @PathVariable UUID eventId
    ) {

        return ResponseEntity.ok(
                eventService.incrementShareCount(eventId)
        );
    }
}