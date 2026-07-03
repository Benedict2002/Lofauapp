package com.codewithben.Lofau.media.dto.response;

import com.codewithben.Lofau.media.enums.MediaType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MediaResponse {

    private UUID id;

    private String url;

    private String thumbnailUrl;

    private MediaType mediaType;

}
