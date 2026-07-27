package com.codewithben.Lofau.advertisement.placement;

import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.advertisement.mapper.AdvertisementMapper;
import com.codewithben.Lofau.advertisement.repository.AdvertisementRepository;
import com.codewithben.Lofau.media.enums.OwnerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdvertisementPlacementServiceImpl
        implements AdvertisementPlacementService,
        AdvertisementPlacementStrategy {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementMapper advertisementMapper;

    @Override
    public List<AdvertisementResponse> getFeedAdvertisements(
            UUID userId,
            OwnerType ownerType
    ) {

        return advertisementRepository.findAll()
                .stream()
                .filter(Advertisement::getApproved)
                .filter(Advertisement::getActive)
                .sorted(Comparator.comparingDouble(this::calculateScore).reversed())
                .limit(5)
                .map(advertisementMapper::toResponse)
                .toList();
    }

    @Override
    public List<AdvertisementResponse> getSidebarAdvertisements(
            UUID userId
    ) {

        return advertisementRepository.findAll()
                .stream()
                .filter(Advertisement::getApproved)
                .filter(Advertisement::getActive)
                .sorted(Comparator.comparingDouble(this::calculateScore).reversed())
                .limit(3)
                .map(advertisementMapper::toResponse)
                .toList();
    }

    @Override
    public List<AdvertisementResponse> getSponsoredCarousel(
            UUID userId
    ) {

        return advertisementRepository.findAll()
                .stream()
                .filter(Advertisement::getApproved)
                .filter(Advertisement::getActive)
                .sorted(Comparator.comparingDouble(this::calculateScore).reversed())
                .limit(10)
                .map(advertisementMapper::toResponse)
                .toList();
    }

    @Override
    public AdvertisementResponse getSponsoredBanner(
            UUID userId
    ) {

        Advertisement advertisement =
                advertisementRepository.findAll()
                        .stream()
                        .filter(Advertisement::getApproved)
                        .filter(Advertisement::getActive)
                        .max(Comparator.comparingDouble(this::calculateScore))
                        .orElseThrow(() -> new RuntimeException("No advertisement available"));

        return advertisementMapper.toResponse(advertisement);
    }

    @Override
    public double calculateScore(
            Advertisement advertisement
    ) {

        double score = 0;

        // Highest priority
        score += advertisement.getPriority() * 100;

        // Campaign budget remaining
        score += (advertisement.getTotalBudget()
                - advertisement.getSpentBudget()) / 10.0;

        // Reward ads that are performing well
        if (advertisement.getImpressions() > 0) {

            double ctr =
                    (double) advertisement.getClicks()
                            / advertisement.getImpressions();

            score += ctr * 50;
        }

        return score;
    }
}