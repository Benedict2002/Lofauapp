package com.codewithben.Lofau.media.service.impl;

import com.codewithben.Lofau.media.dto.response.MediaResponse;
import com.codewithben.Lofau.media.entity.Media;
import com.codewithben.Lofau.media.enums.MediaType;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.repository.MediaRepository;
import com.codewithben.Lofau.media.service.CloudinaryService;
import com.codewithben.Lofau.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<MediaResponse> saveMedia(
            UUID ownerId,
            List<MultipartFile> files,
            OwnerType ownerType
    ) throws IOException {

        List<MediaResponse> responses = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            return responses;
        }

        int sortOrder = 1;

        for (MultipartFile file : files) {

            Map<String, Object> uploadResult =
                    cloudinaryService.uploadFile(file);

            String contentType = file.getContentType();
            String fileName = file.getOriginalFilename();

            MediaType mediaType;

            if (contentType != null && contentType.toLowerCase().startsWith("image")) {
                mediaType = MediaType.IMAGE;
            } else if (contentType != null && contentType.toLowerCase().startsWith("video")) {
                mediaType = MediaType.VIDEO;
            } else if (fileName != null &&
                    (fileName.toLowerCase().endsWith(".jpg")
                            || fileName.toLowerCase().endsWith(".jpeg")
                            || fileName.toLowerCase().endsWith(".png")
                            || fileName.toLowerCase().endsWith(".gif")
                            || fileName.toLowerCase().endsWith(".jfif")
                            || fileName.toLowerCase().endsWith(".webp"))) {

                mediaType = MediaType.IMAGE;

            } else {

                mediaType = MediaType.VIDEO;
            }

            Media media = Media.builder()
                    .ownerId(ownerId)
                    .ownerType(ownerType)
                    .mediaType(mediaType)
                    .url(uploadResult.get("secure_url").toString())
                    .thumbnailUrl(uploadResult.get("secure_url").toString())
                    .publicId(uploadResult.get("public_id").toString())
                    .mimeType(file.getContentType())
                    .fileSize(file.getSize())
                    .sortOrder(sortOrder++)
                    .deleted(false)
                    .build();

            media = mediaRepository.save(media);

            responses.add(
                    MediaResponse.builder()
                            .id(media.getId())
                            .url(media.getUrl())
                            .thumbnailUrl(media.getThumbnailUrl())
                            .mediaType(media.getMediaType())
                            .build()
            );
        }

        return responses;
    }

    @Override
    public List<MediaResponse> getMedia(
            UUID ownerId,
            OwnerType ownerType
    ) {

        List<Media> mediaList =
                mediaRepository
                        .findByOwnerIdAndOwnerTypeAndDeletedFalseOrderBySortOrderAsc(
                                ownerId,
                                ownerType
                        );

        List<MediaResponse> responses = new ArrayList<>();

        for (Media media : mediaList) {

            responses.add(
                    MediaResponse.builder()
                            .id(media.getId())
                            .url(media.getUrl())
                            .thumbnailUrl(media.getThumbnailUrl())
                            .mediaType(media.getMediaType())
                            .build()
            );

        }

        return responses;
    }
}