package com.codewithben.Lofau.event.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateEventRequest {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String location;

    private Double latitude;

    private Double longitude;

    @NotNull
    @Future
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer capacity;

    @NotNull
    private UUID groupId;

}