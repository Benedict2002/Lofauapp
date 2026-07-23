package com.codewithben.Lofau.media.service;

import com.codewithben.Lofau.media.dto.response.MediaResponse;
import com.codewithben.Lofau.media.enums.OwnerType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MediaService {

    /**
     * Upload profile photo
     */
    MediaResponse uploadProfile(
            UUID ownerId,
            MultipartFile file,
            OwnerType ownerType
    ) throws IOException;

    /**
     * Upload cover photo
     */
    MediaResponse uploadCover(
            UUID ownerId,
            MultipartFile file,
            OwnerType ownerType
    ) throws IOException;

    /**
     * Upload gallery images/videos
     */
    List<MediaResponse> uploadGallery(
            UUID ownerId,
            List<MultipartFile> files,
            OwnerType ownerType
    ) throws IOException;

    /**
     * Get profile photo
     */
    MediaResponse getProfile(
            UUID ownerId,
            OwnerType ownerType
    );

    /**
     * Get cover photo
     */
    MediaResponse getCover(
            UUID ownerId,
            OwnerType ownerType
    );

    /**
     * Get gallery
     */
    List<MediaResponse> getGallery(
            UUID ownerId,
            OwnerType ownerType
    );

    /**
     * Delete media
     */
    void deleteMedia(UUID mediaId);
    MediaResponse getMediaById(UUID mediaId);

}