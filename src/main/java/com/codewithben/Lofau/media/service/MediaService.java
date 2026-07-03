package com.codewithben.Lofau.media.service;


import com.codewithben.Lofau.media.dto.response.MediaResponse;
import com.codewithben.Lofau.media.enums.OwnerType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MediaService {

    List<MediaResponse> savePostMedia(

            UUID ownerId,

            List<MultipartFile> files,

            OwnerType ownerType

    ) throws IOException;

    List<MediaResponse> getMedia(

            UUID ownerId,

            OwnerType ownerType

    );

}