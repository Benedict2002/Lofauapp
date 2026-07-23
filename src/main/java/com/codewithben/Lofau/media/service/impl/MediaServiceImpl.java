package com.codewithben.Lofau.media.service.impl;

import com.codewithben.Lofau.media.dto.response.MediaResponse;
import com.codewithben.Lofau.media.entity.Media;
import com.codewithben.Lofau.media.enums.MediaPurpose;
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


    private MediaResponse buildResponse(
            Media media
    ) {

        return MediaResponse.builder()
                .id(media.getId())
                .url(media.getUrl())
                .thumbnailUrl(media.getThumbnailUrl())
                .mediaType(media.getMediaType())
                .build();
    }

    private MediaType detectMediaType(
            MultipartFile file
    ) {

        String contentType = file.getContentType();

        String fileName = file.getOriginalFilename();

        if (contentType != null &&
                contentType.startsWith("image")) {

            return MediaType.IMAGE;
        }

        if (contentType != null &&
                contentType.startsWith("video")) {

            return MediaType.VIDEO;
        }

        if (fileName != null &&
                (
                        fileName.toLowerCase().endsWith(".jpg")
                                || fileName.toLowerCase().endsWith(".jpeg")
                                || fileName.toLowerCase().endsWith(".png")
                                || fileName.toLowerCase().endsWith(".gif")
                                || fileName.toLowerCase().endsWith(".jfif")
                                || fileName.toLowerCase().endsWith(".webp")
                )) {

            return MediaType.IMAGE;
        }

        return MediaType.VIDEO;
    }

    @Override
    public MediaResponse uploadProfile(
            UUID ownerId,
            MultipartFile file,
            OwnerType ownerType
    ) throws IOException {

        return uploadSingleMedia(
                ownerId,
                file,
                ownerType,
                MediaPurpose.PROFILE
        );
    }

    @Override
    public MediaResponse uploadCover(
            UUID ownerId,
            MultipartFile file,
            OwnerType ownerType
    ) throws IOException {

        return uploadSingleMedia(
                ownerId,
                file,
                ownerType,
                MediaPurpose.COVER
        );
    }

    @Override
    public List<MediaResponse> uploadGallery(
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

            Media media = Media.builder()
                    .ownerId(ownerId)
                    .ownerType(ownerType)
                    .purpose(MediaPurpose.GALLERY)
                    .mediaType(detectMediaType(file))
                    .url(uploadResult.get("secure_url").toString())
                    .thumbnailUrl(uploadResult.get("secure_url").toString())
                    .publicId(uploadResult.get("public_id").toString())
                    .mimeType(file.getContentType())
                    .fileSize(file.getSize())
                    .sortOrder(sortOrder++)
                    .deleted(false)
                    .build();

            media = mediaRepository.save(media);

            responses.add(buildResponse(media));
        }

        return responses;
    }
    private MediaResponse uploadSingleMedia(
            UUID ownerId,
            MultipartFile file,
            OwnerType ownerType,
            MediaPurpose purpose
    ) throws IOException {

        mediaRepository
                .findByOwnerIdAndOwnerTypeAndPurposeAndDeletedFalse(
                        ownerId,
                        ownerType,
                        purpose
                )
                .ifPresent(existing -> {

                    try {

                        cloudinaryService.deleteFile(
                                existing.getPublicId()
                        );

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    existing.setDeleted(true);

                    mediaRepository.save(existing);

                });

        Map<String, Object> uploadResult =
                cloudinaryService.uploadFile(file);

        Media media = Media.builder()
                .ownerId(ownerId)
                .ownerType(ownerType)
                .purpose(purpose)
                .mediaType(detectMediaType(file))
                .url(uploadResult.get("secure_url").toString())
                .thumbnailUrl(uploadResult.get("secure_url").toString())
                .publicId(uploadResult.get("public_id").toString())
                .mimeType(file.getContentType())
                .fileSize(file.getSize())
                .sortOrder(1)
                .deleted(false)
                .build();

        media = mediaRepository.save(media);

        return buildResponse(media);
    }





    @Override
    @Transactional(readOnly = true)
    public MediaResponse getProfile(
            UUID ownerId,
            OwnerType ownerType
    ) {

        return mediaRepository
                .findByOwnerIdAndOwnerTypeAndPurposeAndDeletedFalse(
                        ownerId,
                        ownerType,
                        MediaPurpose.PROFILE
                )
                .map(this::buildResponse)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public MediaResponse getCover(
            UUID ownerId,
            OwnerType ownerType
    ) {

        return mediaRepository
                .findByOwnerIdAndOwnerTypeAndPurposeAndDeletedFalse(
                        ownerId,
                        ownerType,
                        MediaPurpose.COVER
                )
                .map(this::buildResponse)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MediaResponse> getGallery(
            UUID ownerId,
            OwnerType ownerType
    ) {
        

        return mediaRepository
                .findByOwnerIdAndOwnerTypeAndPurposeAndDeletedFalseOrderBySortOrderAsc(
                        ownerId,
                        ownerType,
                        MediaPurpose.GALLERY
                )
                .stream()
                .map(this::buildResponse)
                .toList();
    }

    @Override
    public void deleteMedia(UUID mediaId) {

        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() ->
                        new RuntimeException("Media not found"));

        try {

            cloudinaryService.deleteFile(media.getPublicId());

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to delete media from Cloudinary",
                    e
            );
        }

        media.setDeleted(true);

        mediaRepository.save(media);
    }

    @Override
    public MediaResponse getMediaById(UUID mediaId) {
        return null;
    }


//    /**
//     * Existing method - keeps all current modules working.
//     */
//    @Override
//    public List<MediaResponse> saveMedia(
//            UUID ownerId,
//            List<MultipartFile> files,
//            OwnerType ownerType
//    ) throws IOException {
//
//        return saveMedia(
//                ownerId,
//                files,
//                ownerType,
//                MediaPurpose.DEFAULT
//        );
//    }
//
//    /**
//     * New overloaded method supporting media purposes.
//     */
//    @Override
//    public List<MediaResponse> saveMedia(
//            UUID ownerId,
//            List<MultipartFile> files,
//            OwnerType ownerType,
//            MediaPurpose purpose
//    ) throws IOException {
//
//        List<MediaResponse> responses = new ArrayList<>();
//
//        if (files == null || files.isEmpty()) {
//            return responses;
//        }
//
//        int sortOrder = 1;
//
//        for (MultipartFile file : files) {
//
//            System.out.println("Uploading: " + file.getOriginalFilename());
//
//            Map<String, Object> uploadResult =
//                    cloudinaryService.uploadFile(file);
//
//            String contentType = file.getContentType();
//            String fileName = file.getOriginalFilename();
//
//            MediaType mediaType;
//
//            if (contentType != null &&
//                    contentType.toLowerCase().startsWith("image")) {
//
//                mediaType = MediaType.IMAGE;
//
//            } else if (contentType != null &&
//                    contentType.toLowerCase().startsWith("video")) {
//
//                mediaType = MediaType.VIDEO;
//
//            } else if (fileName != null &&
//                    (fileName.toLowerCase().endsWith(".jpg")
//                            || fileName.toLowerCase().endsWith(".jpeg")
//                            || fileName.toLowerCase().endsWith(".png")
//                            || fileName.toLowerCase().endsWith(".gif")
//                            || fileName.toLowerCase().endsWith(".jfif")
//                            || fileName.toLowerCase().endsWith(".webp"))) {
//
//                mediaType = MediaType.IMAGE;
//
//            } else {
//
//                mediaType = MediaType.VIDEO;
//            }
//
//            Media media = Media.builder()
//                    .ownerId(ownerId)
//                    .ownerType(ownerType)
//                    .purpose(purpose)
//                    .mediaType(mediaType)
//                    .url(uploadResult.get("secure_url").toString())
//                    .thumbnailUrl(uploadResult.get("secure_url").toString())
//                    .publicId(uploadResult.get("public_id").toString())
//                    .mimeType(file.getContentType())
//                    .fileSize(file.getSize())
//                    .sortOrder(sortOrder++)
//                    .deleted(false)
//                    .build();
//
//            media = mediaRepository.save(media);
//
//            responses.add(
//                    MediaResponse.builder()
//                            .id(media.getId())
//                            .url(media.getUrl())
//                            .thumbnailUrl(media.getThumbnailUrl())
//                            .mediaType(media.getMediaType())
//                            .build()
//            );
//        }
//
//        return responses;
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public MediaResponse getMedia(
//            UUID ownerId,
//            OwnerType ownerType,
//            MediaPurpose purpose
//    ) {
//
//        Media media = mediaRepository
//                .findByOwnerIdAndOwnerTypeAndPurposeAndDeletedFalse(
//                        ownerId,
//                        ownerType,
//                        purpose
//                )
//                .orElse(null);
//
//        if (media == null) {
//            return null;
//        }
//
//        return MediaResponse.builder()
//                .id(media.getId())
//                .url(media.getUrl())
//                .thumbnailUrl(media.getThumbnailUrl())
//                .mediaType(media.getMediaType())
//                .build();
//    }
}