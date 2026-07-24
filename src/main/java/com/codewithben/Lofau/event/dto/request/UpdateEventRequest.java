package com.codewithben.Lofau.event.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateEventRequest {

    private String title;

    private String description;

    private String location;

    private Double latitude;

    private Double longitude;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer capacity;

    private Boolean active;

    private Boolean cancelled;

    private Boolean featured;

    private Boolean commentsEnabled;

}