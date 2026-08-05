package com.codewithben.Lofau.advertisement.service;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.advertisement.analytics.AdvertisementAnalyticsService;
import com.codewithben.Lofau.advertisement.dto.request.CreateAdvertisementRequest;
import com.codewithben.Lofau.advertisement.dto.request.UpdateAdvertisementRequest;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementDashboardResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementStatisticsResponse;
import com.codewithben.Lofau.advertisement.entity.Advertisement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementPlacement;
import com.codewithben.Lofau.advertisement.enums.AdvertisementStatus;
import com.codewithben.Lofau.advertisement.mapper.AdvertisementMapper;
import com.codewithben.Lofau.advertisement.placement.AdvertisementPlacementService;
import com.codewithben.Lofau.advertisement.repository.AdvertisementRepository;
import com.codewithben.Lofau.advertisement.service.impl.AdvertisementServiceImpl;
import com.codewithben.Lofau.advertisement.targeting.AdvertisementTargetingService;
import com.codewithben.Lofau.advertisement.validator.AdvertisementValidator;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import com.codewithben.Lofau.media.enums.MediaType;
import com.codewithben.Lofau.media.service.MediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@ExtendWith(MockitoExtension.class)
class AdvertisementServiceImplTest {

    @Mock
    private AdvertisementRepository advertisementRepository;

    @Mock
    private AdvertisementMapper advertisementMapper;

    @Mock
    private AdvertisementValidator advertisementValidator;

    @Mock
    private AdvertisementAnalyticsService analyticsService;

    @Mock
    private AdvertisementPlacementService placementService;

    @Mock
    private AdvertisementTargetingService targetingService;

    @Mock
    private MediaService mediaService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdvertisementServiceImpl advertisementService;

    private User user;

    private Advertisement advertisement;

    private CreateAdvertisementRequest request;

    private AdvertisementResponse response;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(UUID.randomUUID())
                .email("test@test.com")
                .build();

        advertisement = Advertisement.builder()
                .id(UUID.randomUUID())
                .title("Samsung")
                .build();

        request = CreateAdvertisementRequest.builder()
                .title("Samsung")
                .description("New phone")
                .build();

        response = AdvertisementResponse.builder()
                .id(advertisement.getId())
                .title("Samsung")
                .build();
    }

    @Test
    void shouldCreateAdvertisementSuccessfully() throws Exception {

        Authentication authentication = mock(Authentication.class);

        SecurityContext securityContext = mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn("test@test.com");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(advertisementMapper.toEntity(request))
                .thenReturn(advertisement);

        when(advertisementRepository.save(any(Advertisement.class)))
                .thenReturn(advertisement);

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        AdvertisementResponse result =
                advertisementService.createAdvertisement(request);

        assertNotNull(result);

        assertEquals("Samsung", result.getTitle());

        verify(advertisementRepository)
                .save(any(Advertisement.class));

        verify(advertisementValidator)
                .validateForCreation(any());

        verify(advertisementMapper)
                .toResponse(advertisement);
    }

    @Test
    void givenAdvertisementWithMedia_whenCreateAdvertisement_thenMediaIsUploaded() throws Exception {

        /*
         * Arrange
         * ----------------------------------------
         * Prepare all dependencies required for
         * creating an advertisement with media.
         */

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        when(authentication.getName())
                .thenReturn("test@test.com");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(advertisementMapper.toEntity(request))
                .thenReturn(advertisement);

        when(advertisementRepository.save(any(Advertisement.class)))
                .thenReturn(advertisement);

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        /*
         * Mock an uploaded image.
         */
        MockMultipartFile file =
                new MockMultipartFile(
                        "media",
                        "phone.jpg",
                        "image/jpeg",
                        "image".getBytes()
                );

        request.setMedia(file);

        /*
         * Mock the response returned after uploading
         * the advertisement media.
         */
        MediaResponse mediaResponse =
                MediaResponse.builder()
                        .id(UUID.randomUUID())
                        .url("https://cloudinary.com/image.jpg")
                        .thumbnailUrl("https://cloudinary.com/thumb.jpg")
                        .mediaType(MediaType.IMAGE)
                        .build();

        when(mediaService.uploadAdvertisement(
                advertisement.getId(),
                file
        )).thenReturn(mediaResponse);

        /*
         * Act
         * ----------------------------------------
         * Execute the method under test.
         */
        AdvertisementResponse result =
                advertisementService.createAdvertisement(request);

        /*
         * Assert
         * ----------------------------------------
         * Verify the advertisement was created
         * and the media upload service was invoked.
         */
        assertNotNull(result);

        verify(mediaService)
                .uploadAdvertisement(
                        advertisement.getId(),
                        file
                );

        verify(advertisementRepository)
                .save(any(Advertisement.class));
    }

    /**
     * Verifies that an advertiser can successfully update
     * one of their own advertisements before it has been approved.
     */
    @Test
    void shouldUpdateAdvertisementSuccessfully() throws IOException {

        // Arrange
        UUID advertisementId = UUID.randomUUID();

        UpdateAdvertisementRequest request =
                new UpdateAdvertisementRequest();

        request.setTitle("Updated Samsung");

        User advertiser = User.builder()
                .id(UUID.randomUUID())
                .email("ben@test.com")
                .build();

        Advertisement advertisement =
                Advertisement.builder()
                        .id(advertisementId)
                        .advertiser(advertiser)
                        .approved(false)
                        .build();

        AdvertisementResponse response =
                AdvertisementResponse.builder()
                        .id(advertisementId)
                        .title("Updated Samsung")
                        .build();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.of(advertisement));

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(advertiser));

        when(advertisementRepository.save(any()))
                .thenReturn(advertisement);

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        // Act
        AdvertisementResponse result =
                advertisementService.updateAdvertisement(
                        advertisementId,
                        request
                );

        // Assert
        assertNotNull(result);
        assertEquals(advertisementId, result.getId());

        verify(advertisementMapper)
                .updateEntity(advertisement, request);

        verify(advertisementValidator)
                .validateForCreation(advertisement);

        verify(advertisementRepository)
                .save(advertisement);
    }

    /**
     * Verifies that updating a non-existent advertisement
     * throws an exception.
     */
    @Test
    void shouldThrowWhenAdvertisementDoesNotExist() {

        UUID advertisementId = UUID.randomUUID();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> advertisementService.updateAdvertisement(
                                advertisementId,
                                new UpdateAdvertisementRequest()
                        )
                );

        assertEquals(
                "Advertisement not found.",
                exception.getMessage()
        );
    }

    /**
     * Verifies that a user cannot update
     * another user's advertisement.
     */
    @Test
    void shouldThrowWhenUserIsNotOwner() {

        UUID advertisementId = UUID.randomUUID();

        User owner = User.builder()
                .id(UUID.randomUUID())
                .build();

        User attacker = User.builder()
                .id(UUID.randomUUID())
                .email("attacker@test.com")
                .build();

        Advertisement advertisement =
                Advertisement.builder()
                        .id(advertisementId)
                        .advertiser(owner)
                        .approved(false)
                        .build();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.of(advertisement));

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(attacker));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> advertisementService.updateAdvertisement(
                                advertisementId,
                                new UpdateAdvertisementRequest()
                        )
                );

        assertEquals(
                "You are not allowed to update this advertisement.",
                exception.getMessage()
        );
    }

    /**
     * Verifies that approved advertisements
     * cannot be edited.
     */
    @Test
    void shouldThrowWhenAdvertisementIsApproved() {

        UUID advertisementId = UUID.randomUUID();

        User advertiser = User.builder()
                .id(UUID.randomUUID())
                .email("ben@test.com")
                .build();

        Advertisement advertisement =
                Advertisement.builder()
                        .id(advertisementId)
                        .advertiser(advertiser)
                        .approved(true)
                        .build();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.of(advertisement));

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(advertiser));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> advertisementService.updateAdvertisement(
                                advertisementId,
                                new UpdateAdvertisementRequest()
                        )
                );

        assertEquals(
                "Approved advertisements cannot be edited.",
                exception.getMessage()
        );
    }

    /**
     * Verifies that retrieving an advertisement:
     * - finds the advertisement
     * - records an impression
     * - maps it into a response DTO.
     */
    @Test
    void shouldGetAdvertisementSuccessfully() {

        // Arrange
        UUID advertisementId = UUID.randomUUID();

        Advertisement advertisement =
                Advertisement.builder()
                        .id(advertisementId)
                        .title("Samsung S24")
                        .build();

        AdvertisementResponse response =
                AdvertisementResponse.builder()
                        .id(advertisementId)
                        .title("Samsung S24")
                        .build();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.of(advertisement));

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        // Act
        AdvertisementResponse result =
                advertisementService.getAdvertisement(advertisementId);

        // Assert
        assertNotNull(result);
        assertEquals(advertisementId, result.getId());

        verify(analyticsService)
                .recordImpression(advertisementId);

        verify(advertisementMapper)
                .toResponse(advertisement);
    }
    /**
     * Verifies that requesting a non-existent advertisement
     * throws an exception.
     */
    @Test
    void shouldThrowWhenGettingNonExistingAdvertisement() {

        UUID advertisementId = UUID.randomUUID();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> advertisementService.getAdvertisement(
                                advertisementId
                        )
                );

        assertEquals(
                "Advertisement not found.",
                exception.getMessage()
        );

        verifyNoInteractions(analyticsService);
    }


    /**
     * Verifies that only advertisements that are not deleted
     * are returned to the client.
     */
    @Test
    void shouldReturnOnlyNonDeletedAdvertisements() {

        // Arrange
        Advertisement activeAdvertisement =
                Advertisement.builder()
                        .id(UUID.randomUUID())
                        .title("Samsung")
                        .deleted(false)
                        .build();

        Advertisement deletedAdvertisement =
                Advertisement.builder()
                        .id(UUID.randomUUID())
                        .title("Deleted Ad")
                        .deleted(true)
                        .build();

        AdvertisementResponse response =
                AdvertisementResponse.builder()
                        .id(activeAdvertisement.getId())
                        .title("Samsung")
                        .build();

        when(advertisementRepository.findAll())
                .thenReturn(List.of(
                        activeAdvertisement,
                        deletedAdvertisement
                ));

        when(advertisementMapper.toResponse(activeAdvertisement))
                .thenReturn(response);

        // Act
        List<AdvertisementResponse> results =
                advertisementService.getAdvertisements();

        // Assert
        assertEquals(1, results.size());
        assertEquals(
                activeAdvertisement.getId(),
                results.get(0).getId()
        );

        verify(advertisementMapper)
                .toResponse(activeAdvertisement);

        verify(advertisementMapper, never())
                .toResponse(deletedAdvertisement);
    }

    /**
     * Verifies that an empty list is returned
     * when there are no advertisements.
     */
    @Test
    void shouldReturnEmptyListWhenNoAdvertisementsExist() {

        // Arrange
        when(advertisementRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<AdvertisementResponse> results =
                advertisementService.getAdvertisements();

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());

        verifyNoInteractions(advertisementMapper);
    }
    /**
     * Verifies that every non-deleted advertisement
     * is converted into a response DTO.
     */
    @Test
    void shouldMapEveryAdvertisement() {

        // Arrange
        Advertisement ad1 =
                Advertisement.builder()
                        .id(UUID.randomUUID())
                        .deleted(false)
                        .build();

        Advertisement ad2 =
                Advertisement.builder()
                        .id(UUID.randomUUID())
                        .deleted(false)
                        .build();

        AdvertisementResponse response1 =
                AdvertisementResponse.builder()
                        .id(ad1.getId())
                        .build();

        AdvertisementResponse response2 =
                AdvertisementResponse.builder()
                        .id(ad2.getId())
                        .build();

        when(advertisementRepository.findAll())
                .thenReturn(List.of(ad1, ad2));

        when(advertisementMapper.toResponse(ad1))
                .thenReturn(response1);

        when(advertisementMapper.toResponse(ad2))
                .thenReturn(response2);

        // Act
        List<AdvertisementResponse> results =
                advertisementService.getAdvertisements();

        // Assert
        assertEquals(2, results.size());

        verify(advertisementMapper)
                .toResponse(ad1);

        verify(advertisementMapper)
                .toResponse(ad2);
    }

    /**
     * Verifies that the authenticated user's advertisements
     * are returned and mapped correctly.
     */
    @Test
    void shouldReturnCurrentUsersAdvertisements() {

        // Arrange
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("ben@gmail.com")
                .build();

        Authentication authentication =
                mock(Authentication.class);

        SecurityContext securityContext =
                mock(SecurityContext.class);

        when(authentication.getName())
                .thenReturn(user.getEmail());

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Advertisement advertisement =
                Advertisement.builder()
                        .id(UUID.randomUUID())
                        .title("Samsung")
                        .build();

        AdvertisementResponse response =
                AdvertisementResponse.builder()
                        .id(advertisement.getId())
                        .title("Samsung")
                        .build();

        when(advertisementRepository
                .findByAdvertiserAndDeletedFalse(user))
                .thenReturn(List.of(advertisement));

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        // Act
        List<AdvertisementResponse> results =
                advertisementService.getMyAdvertisements();

        // Assert
        assertEquals(1, results.size());
        assertEquals(
                advertisement.getId(),
                results.get(0).getId()
        );

        verify(advertisementRepository)
                .findByAdvertiserAndDeletedFalse(user);

        verify(advertisementMapper)
                .toResponse(advertisement);
    }

    /**
     * Verifies that an empty list is returned
     * when the authenticated user has no advertisements.
     */
    @Test
    void shouldReturnEmptyListWhenUserHasNoAdvertisements() {

        // Arrange
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("ben@gmail.com")
                .build();

        Authentication authentication =
                mock(Authentication.class);

        SecurityContext securityContext =
                mock(SecurityContext.class);

        when(authentication.getName())
                .thenReturn(user.getEmail());

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(advertisementRepository
                .findByAdvertiserAndDeletedFalse(user))
                .thenReturn(List.of());

        // Act
        List<AdvertisementResponse> results =
                advertisementService.getMyAdvertisements();

        // Assert
        assertTrue(results.isEmpty());

        verifyNoInteractions(advertisementMapper);
    }

    /**
     * Verifies that an exception is thrown
     * when the authenticated user cannot be found.
     */
    @Test
    void shouldThrowWhenCurrentUserDoesNotExist() {

        // Arrange
        Authentication authentication =
                mock(Authentication.class);

        SecurityContext securityContext =
                mock(SecurityContext.class);

        when(authentication.getName())
                .thenReturn("missing@gmail.com");

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("missing@gmail.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> advertisementService.getMyAdvertisements()
                );

        assertEquals(
                "User not found.",
                exception.getMessage()
        );
    }

    /**
     * Verifies that advertisements belonging to the specified
     * placement are returned and mapped correctly.
     */
    @Test
    void shouldReturnAdvertisementsByPlacement() {

        // Arrange
        Advertisement advertisement =
                Advertisement.builder()
                        .id(UUID.randomUUID())
                        .placement(AdvertisementPlacement.HOME_FEED)
                        .deleted(false)
                        .build();

        AdvertisementResponse response =
                AdvertisementResponse.builder()
                        .id(advertisement.getId())
                        .build();

        when(advertisementRepository.findByPlacementAndDeletedFalse(
                AdvertisementPlacement.HOME_FEED))
                .thenReturn(List.of(advertisement));

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        // Act
        List<AdvertisementResponse> results =
                advertisementService.getAdvertisementsByPlacement("HOME_FEED");

        // Assert
        assertEquals(1, results.size());

        verify(advertisementRepository)
                .findByPlacementAndDeletedFalse(
                        AdvertisementPlacement.HOME_FEED
                );

        verify(advertisementMapper)
                .toResponse(advertisement);
    }

    /**
     * Verifies that an empty list is returned
     * when no advertisements exist for the placement.
     */
    @Test
    void shouldReturnEmptyListWhenPlacementHasNoAdvertisements() {

        // Arrange
        when(advertisementRepository.findByPlacementAndDeletedFalse(
                AdvertisementPlacement.HOME_FEED))
                .thenReturn(List.of());

        // Act
        List<AdvertisementResponse> results =
                advertisementService.getAdvertisementsByPlacement("HOME_FEED");

        // Assert
        assertTrue(results.isEmpty());

        verifyNoInteractions(advertisementMapper);
    }

    /**
     * Verifies that an invalid placement value
     * throws an IllegalArgumentException.
     */
    @Test
    void shouldThrowWhenPlacementIsInvalid() {

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> advertisementService.getAdvertisementsByPlacement("INVALID")
        );

        verifyNoInteractions(advertisementRepository);
    }

    /**
     * Verifies that placement names are treated
     * case-insensitively.
     */
    @Test
    void shouldAcceptLowerCasePlacement() {

        // Arrange
        Advertisement advertisement =
                Advertisement.builder()
                        .id(UUID.randomUUID())
                        .placement(AdvertisementPlacement.HOME_FEED)
                        .build();

        AdvertisementResponse response =
                AdvertisementResponse.builder()
                        .id(advertisement.getId())
                        .build();

        when(advertisementRepository.findByPlacementAndDeletedFalse(
                AdvertisementPlacement.HOME_FEED))
                .thenReturn(List.of(advertisement));

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        // Act
        List<AdvertisementResponse> results =
                advertisementService.getAdvertisementsByPlacement("home_feed");

        // Assert
        assertEquals(1, results.size());
    }

    /**
     * Verifies that a valid advertisement is approved
     * and the updated advertisement is returned.
     */
    @Test
    void shouldApproveAdvertisement() {

        // Arrange
        UUID advertisementId = UUID.randomUUID();

        Advertisement advertisement =
                Advertisement.builder()
                        .id(advertisementId)
                        .approved(false)
                        .build();

        AdvertisementResponse response =
                AdvertisementResponse.builder()
                        .id(advertisementId)
                        .approved(true)
                        .build();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.of(advertisement));

        when(advertisementRepository.save(advertisement))
                .thenReturn(advertisement);

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        // Act
        AdvertisementResponse result =
                advertisementService.approveAdvertisement(advertisementId);

        // Assert
        assertTrue(advertisement.getApproved());
        assertTrue(result.getApproved());

        verify(advertisementValidator)
                .validateForApproval(advertisement);

        verify(advertisementRepository)
                .save(advertisement);

        verify(advertisementMapper)
                .toResponse(advertisement);
    }

    /**
     * Verifies that approving a non-existing advertisement
     * throws an exception.
     */
    @Test
    void shouldThrowWhenApprovingMissingAdvertisement() {

        // Arrange
        UUID advertisementId = UUID.randomUUID();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> advertisementService.approveAdvertisement(advertisementId)
                );

        assertEquals(
                "Advertisement not found.",
                exception.getMessage()
        );

        verifyNoInteractions(
                advertisementValidator,
                advertisementMapper
        );
    }

    /**
     * Verifies that approval fails when
     * the validator rejects the advertisement.
     */
    @Test
    void shouldNotApproveWhenValidationFails() {

        // Arrange
        UUID advertisementId = UUID.randomUUID();

        Advertisement advertisement =
                Advertisement.builder()
                        .id(advertisementId)
                        .approved(false)
                        .build();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.of(advertisement));

        doThrow(new RuntimeException("Advertisement cannot be approved"))
                .when(advertisementValidator)
                .validateForApproval(advertisement);

        // Act & Assert
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> advertisementService.approveAdvertisement(advertisementId)
                );

        assertEquals(
                "Advertisement cannot be approved",
                exception.getMessage()
        );

        verify(advertisementRepository, never())
                .save(any());

        verify(advertisementMapper, never())
                .toResponse(any());
    }
    /**
     * Verifies that an advertisement can be rejected
     * successfully.
     */
    @Test
    void shouldRejectAdvertisement() {

        // Arrange
        UUID advertisementId = UUID.randomUUID();

        Advertisement advertisement =
                Advertisement.builder()
                        .id(advertisementId)
                        .approved(true)
                        .status(AdvertisementStatus.PENDING_APPROVAL)
                        .build();

        AdvertisementResponse response =
                AdvertisementResponse.builder()
                        .id(advertisementId)
                        .approved(false)
                        .status(AdvertisementStatus.REJECTED)
                        .build();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.of(advertisement));

        when(advertisementRepository.save(advertisement))
                .thenReturn(advertisement);

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        // Act
        AdvertisementResponse result =
                advertisementService.rejectAdvertisement(advertisementId);

        // Assert
        assertFalse(advertisement.getApproved());
        assertEquals(
                AdvertisementStatus.REJECTED,
                advertisement.getStatus()
        );

        assertFalse(result.getApproved());
        assertEquals(
                AdvertisementStatus.REJECTED,
                result.getStatus()
        );

        verify(advertisementRepository)
                .save(advertisement);

        verify(advertisementMapper)
                .toResponse(advertisement);
    }

    /**
     * Verifies that rejecting a non-existing advertisement
     * throws an exception.
     */
    @Test
    void shouldThrowWhenRejectingMissingAdvertisement() {

        // Arrange
        UUID advertisementId = UUID.randomUUID();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> advertisementService.rejectAdvertisement(advertisementId)
                );

        assertEquals(
                "Advertisement not found.",
                exception.getMessage()
        );

        verify(advertisementRepository, never())
                .save(any());

        verify(advertisementMapper, never())
                .toResponse(any());
    }

    /**
     * Should activate an approved advertisement.
     */
    @Test
    void activateAdvertisement_ShouldActivateAdvertisement() {

        UUID advertisementId = UUID.randomUUID();

        Advertisement advertisement = Advertisement.builder()
                .id(advertisementId)
                .approved(true)
                .active(false)
                .build();

        AdvertisementResponse response =
                AdvertisementResponse.builder()
                        .id(advertisementId)
                        .build();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.of(advertisement));

        doNothing()
                .when(advertisementValidator)
                .validateForActivation(advertisement);

        when(advertisementRepository.save(advertisement))
                .thenReturn(advertisement);

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        AdvertisementResponse result =
                advertisementService.activateAdvertisement(advertisementId);

        assertNotNull(result);
        assertEquals(advertisementId, result.getId());

        assertTrue(advertisement.getActive());
        assertEquals(
                AdvertisementStatus.ACTIVE,
                advertisement.getStatus()
        );

        verify(advertisementValidator)
                .validateForActivation(advertisement);

        verify(advertisementRepository)
                .save(advertisement);

        verify(advertisementMapper)
                .toResponse(advertisement);
    }

    /**
     * Should throw an exception when the advertisement does not exist.
     */
    @Test
    void activateAdvertisement_ShouldThrow_WhenAdvertisementNotFound() {

        UUID advertisementId = UUID.randomUUID();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> advertisementService.activateAdvertisement(advertisementId)
                );

        assertEquals(
                "Advertisement not found.",
                exception.getMessage()
        );

        verify(advertisementRepository)
                .findById(advertisementId);

        verifyNoMoreInteractions(advertisementRepository);
    }

    /**
     * Should propagate validation errors when activation is not allowed.
     */
    @Test
    void activateAdvertisement_ShouldThrow_WhenValidationFails() {

        UUID advertisementId = UUID.randomUUID();

        Advertisement advertisement =
                Advertisement.builder()
                        .id(advertisementId)
                        .approved(false)
                        .build();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.of(advertisement));

        doThrow(new RuntimeException("Advertisement has not been approved."))
                .when(advertisementValidator)
                .validateForActivation(advertisement);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> advertisementService.activateAdvertisement(advertisementId)
                );

        assertEquals(
                "Advertisement has not been approved.",
                exception.getMessage()
        );

        verify(advertisementValidator)
                .validateForActivation(advertisement);

        verify(advertisementRepository, never())
                .save(any());
    }

    /**
     * Should deactivate an advertisement successfully.
     */
    @Test
    void deactivateAdvertisement_ShouldDeactivateAdvertisement() {

        UUID advertisementId = UUID.randomUUID();

        Advertisement advertisement = Advertisement.builder()
                .id(advertisementId)
                .active(true)
                .status(AdvertisementStatus.ACTIVE)
                .build();

        AdvertisementResponse response = AdvertisementResponse.builder()
                .id(advertisementId)
                .build();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.of(advertisement));

        when(advertisementRepository.save(advertisement))
                .thenReturn(advertisement);

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        AdvertisementResponse result =
                advertisementService.deactivateAdvertisement(advertisementId);

        assertNotNull(result);
        assertEquals(advertisementId, result.getId());

        assertFalse(advertisement.getActive());
        assertEquals(
                AdvertisementStatus.INACTIVE,
                advertisement.getStatus()
        );

        verify(advertisementRepository).save(advertisement);
        verify(advertisementMapper).toResponse(advertisement);
    }
    /**
     * Should throw an exception when the advertisement does not exist.
     */
    @Test
    void deactivateAdvertisement_ShouldThrow_WhenAdvertisementNotFound() {

        UUID advertisementId = UUID.randomUUID();

        when(advertisementRepository.findById(advertisementId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> advertisementService.deactivateAdvertisement(advertisementId)
        );

        assertEquals(
                "Advertisement not found.",
                exception.getMessage()
        );

        verify(advertisementRepository).findById(advertisementId);
        verify(advertisementRepository, never()).save(any());
    }
    /**
     * Should return all advertisements that are pending approval.
     */
    @Test
    void shouldReturnPendingAdvertisements() {

        // Arrange
        Advertisement advertisement = Advertisement.builder()
                .approved(false)
                .deleted(false)
                .build();

        AdvertisementResponse response =
                AdvertisementResponse.builder().build();

        when(advertisementRepository.findByApprovedFalseAndDeletedFalse())
                .thenReturn(List.of(advertisement));

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        // Act
        List<AdvertisementResponse> result =
                advertisementService.getPendingAdvertisements();

        // Assert
        assertEquals(1, result.size());

        verify(advertisementRepository)
                .findByApprovedFalseAndDeletedFalse();

        verify(advertisementMapper)
                .toResponse(advertisement);
    }
    /**
     * Should return all active advertisements.
     */
    @Test
    void shouldReturnActiveAdvertisements() {

        Advertisement advertisement = Advertisement.builder()
                .approved(true)
                .active(true)
                .deleted(false)
                .build();

        AdvertisementResponse response =
                AdvertisementResponse.builder().build();

        when(advertisementRepository
                .findByApprovedTrueAndActiveTrueAndDeletedFalse())
                .thenReturn(List.of(advertisement));

        when(advertisementMapper.toResponse(advertisement))
                .thenReturn(response);

        List<AdvertisementResponse> result =
                advertisementService.getActiveAdvertisements();

        assertEquals(1, result.size());

        verify(advertisementRepository)
                .findByApprovedTrueAndActiveTrueAndDeletedFalse();

        verify(advertisementMapper)
                .toResponse(advertisement);
    }

    /**
     * Should delegate click recording to the analytics service.
     */
    @Test
    void shouldRecordClick() {

        UUID advertisementId = UUID.randomUUID();

        advertisementService.recordClick(advertisementId);

        verify(analyticsService)
                .recordClick(advertisementId);
    }

    /**
     * Should delegate impression recording to the analytics service.
     */
    @Test
    void shouldRecordImpression() {

        UUID advertisementId = UUID.randomUUID();

        advertisementService.recordImpression(advertisementId);

        verify(analyticsService)
                .recordImpression(advertisementId);
    }

    /**
     * Should return advertisement statistics with calculated CTR.
     */
    @Test
    void shouldReturnAdvertisementStatistics() {

        Advertisement advertisement = Advertisement.builder()
                .impressions(200)
                .clicks(20)
                .totalBudget(1000)
                .spentBudget(250)
                .build();

        when(advertisementRepository.findById(any()))
                .thenReturn(Optional.of(advertisement));

        AdvertisementStatisticsResponse result =
                advertisementService.getAdvertisementStatistics(
                        UUID.randomUUID()
                );

        assertEquals(200, result.getImpressions());
        assertEquals(20, result.getClicks());
        assertEquals(10.0, result.getCtr());
        assertEquals(1000, result.getTotalBudget());
        assertEquals(250, result.getSpentBudget());
        assertEquals(750, result.getRemainingBudget());
    }

    /**
     * Should return dashboard statistics for the authenticated advertiser.
     */
    @Test
    void shouldReturnDashboard() {

        User advertiser = User.builder()
                .id(UUID.randomUUID())
                .email("test@test.com")
                .build();

        Advertisement ad1 = Advertisement.builder()
                .impressions(100)
                .clicks(10)
                .spentBudget(100)
                .active(true)
                .approved(true)
                .build();

        Advertisement ad2 = Advertisement.builder()
                .impressions(50)
                .clicks(5)
                .spentBudget(50)
                .active(false)
                .approved(false)
                .build();

        Authentication authentication =
                mock(Authentication.class);

        SecurityContext context =
                mock(SecurityContext.class);

        when(context.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        when(authentication.getName())
                .thenReturn("test@test.com");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(advertiser));

        when(advertisementRepository.findByAdvertiser(advertiser))
                .thenReturn(List.of(ad1, ad2));

        AdvertisementDashboardResponse dashboard =
                advertisementService.getDashboard();

        assertEquals(2, dashboard.getTotalAdvertisements());
        assertEquals(1, dashboard.getActiveAdvertisements());
        assertEquals(1, dashboard.getPendingAdvertisements());
        assertEquals(150, dashboard.getTotalImpressions());
        assertEquals(15, dashboard.getTotalClicks());
        assertEquals(150, dashboard.getTotalSpent());
        assertEquals(10.0, dashboard.getAverageCTR());
    }

}
