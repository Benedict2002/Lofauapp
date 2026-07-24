package com.codewithben.Lofau.event.dto.response;


import com.codewithben.Lofau.media.dto.response.MediaResponse;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EventAttendeeResponse {

    private UUID id;

    private String username;

    private MediaResponse profileImage;
}