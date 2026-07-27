package com.codewithben.Lofau.advertisement.mapper;

import com.codewithben.Lofau.advertisement.dto.request.CreateAdvertisementRequest;
import com.codewithben.Lofau.advertisement.dto.request.UpdateAdvertisementRequest;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdvertisementMapper {

    private final MediaService mediaService;

    public Advertisement toEntity(
            CreateAdvertisementRequest request
    ) {

        if (request == null) {
            return null;
        }

        return Advertisement.builder()

                .title(request.getTitle())
                .description(request.getDescription())

                .websiteUrl(request.getWebsiteUrl())
                .callToAction(request.getCallToAction())

                .type(request.getType())
                .placement(request.getPlacement())

                .priority(request.getPriority())

                .dailyLimit(request.getDailyLimit())
                .totalBudget(request.getTotalBudget())

                .startDate(request.getStartDate())
                .endDate(request.getEndDate())

                .build();
    }

    public void updateEntity(
            Advertisement advertisement,
            UpdateAdvertisementRequest request
    ) {

        if (request.getTitle() != null) {
            advertisement.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            advertisement.setDescription(request.getDescription());
        }

        if (request.getWebsiteUrl() != null) {
            advertisement.setWebsiteUrl(request.getWebsiteUrl());
        }

        if (request.getCallToAction() != null) {
            advertisement.setCallToAction(request.getCallToAction());
        }

        if (request.getType() != null) {
            advertisement.setType(request.getType());
        }

        if (request.getPlacement() != null) {
            advertisement.setPlacement(request.getPlacement());
        }

        if (request.getStatus() != null) {
            advertisement.setStatus(request.getStatus());
        }

        if (request.getPriority() != null) {
            advertisement.setPriority(request.getPriority());
        }

        if (request.getDailyLimit() != null) {
            advertisement.setDailyLimit(request.getDailyLimit());
        }

        if (request.getTotalBudget() != null) {
            advertisement.setTotalBudget(request.getTotalBudget());
        }

        if (request.getActive() != null) {
            advertisement.setActive(request.getActive());
        }

        if (request.getStartDate() != null) {
            advertisement.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            advertisement.setEndDate(request.getEndDate());
        }
    }

    public AdvertisementResponse toResponse(
            Advertisement advertisement
    ) {

        List<MediaResponse> gallery =
                mediaService.getGallery(
                        advertisement.getId(),
                        OwnerType.ADVERTISEMENT
                );

        MediaResponse coverImage =
                mediaService.getCover(
                        advertisement.getId(),
                        OwnerType.ADVERTISEMENT
                );

        MediaResponse previewImage =
                !gallery.isEmpty()
                        ? gallery.get(0)
                        : coverImage;

        return AdvertisementResponse.builder()

                .id(advertisement.getId())

                .title(advertisement.getTitle())
                .description(advertisement.getDescription())

                .websiteUrl(advertisement.getWebsiteUrl())
                .callToAction(advertisement.getCallToAction())

                .type(advertisement.getType())
                .placement(advertisement.getPlacement())
                .status(advertisement.getStatus())

                .priority(advertisement.getPriority())

                .impressions(advertisement.getImpressions())
                .clicks(advertisement.getClicks())

                .dailyLimit(advertisement.getDailyLimit())
                .totalBudget(advertisement.getTotalBudget())
                .spentBudget(advertisement.getSpentBudget())

                .active(advertisement.getActive())
                .approved(advertisement.getApproved())

                // Advertiser

                .advertiserId(
                        advertisement.getAdvertiser().getId()
                )

                .advertiserUsername(
                        advertisement.getAdvertiser()
                                .getDisplayUsername()
                )

                .advertiserProfileImage(
                        mediaService.getProfile(
                                advertisement.getAdvertiser().getId(),
                                OwnerType.USER
                        )
                )

                // Media

                .coverImage(coverImage)
                .previewImage(previewImage)
                .gallery(gallery)
                .mediaCount(gallery.size())

                .startDate(advertisement.getStartDate())
                .endDate(advertisement.getEndDate())

                .createdAt(advertisement.getCreatedAt())
                .updatedAt(advertisement.getUpdatedAt())

                .build();
    }
}
