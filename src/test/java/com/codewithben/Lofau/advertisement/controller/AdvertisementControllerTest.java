package com.codewithben.Lofau.advertisement.controller;

import com.codewithben.Lofau.Auth.jwt.JwtAuthenticationFilter;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.advertisement.controller.AdvertisementController;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementDashboardResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementResponse;
import com.codewithben.Lofau.advertisement.dto.response.AdvertisementStatisticsResponse;
import com.codewithben.Lofau.advertisement.feed.AdvertisementFeedService;
import com.codewithben.Lofau.advertisement.service.AdvertisementService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdvertisementController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdvertisementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdvertisementService advertisementService;

    @MockitoBean
    private AdvertisementFeedService advertisementFeedService;

    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Should return a single advertisement.
     */
    @Test
    void shouldGetAdvertisement() throws Exception {

        UUID id = UUID.randomUUID();

        AdvertisementResponse response =
                AdvertisementResponse.builder()
                        .id(id)
                        .title("Samsung")
                        .build();

        when(advertisementService.getAdvertisement(id))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/advertisements/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Samsung"));

        verify(advertisementService)
                .getAdvertisement(id);
    }

    /**
     * Should return all advertisements.
     */
    @Test
    void shouldGetAdvertisements() throws Exception {

        AdvertisementResponse response =
                AdvertisementResponse.builder()
                        .title("Samsung")
                        .build();

        when(advertisementService.getAdvertisements())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/advertisements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title")
                        .value("Samsung"));

        verify(advertisementService)
                .getAdvertisements();
    }

    /**
     * Should return advertisements
     * belonging to the authenticated user.
     */
    @Test
    void shouldGetMyAdvertisements() throws Exception {

        AdvertisementResponse response =
                AdvertisementResponse.builder()
                        .title("My Ad")
                        .build();

        when(advertisementService.getMyAdvertisements())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/advertisements/mine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title")
                        .value("My Ad"));

        verify(advertisementService)
                .getMyAdvertisements();
    }

    /**
     * Should delete an advertisement.
     */
    @Test
    void shouldDeleteAdvertisement() throws Exception {

        UUID id = UUID.randomUUID();

        doNothing().when(advertisementService)
                .deleteAdvertisement(id);

        mockMvc.perform(delete("/api/v1/advertisements/{id}", id))
                .andExpect(status().isNoContent());

        verify(advertisementService)
                .deleteAdvertisement(id);
    }

    /**
     * Should record advertisement click.
     */
    @Test
    void shouldRecordClick() throws Exception {

        UUID id = UUID.randomUUID();

        doNothing().when(advertisementService)
                .recordClick(id);

        mockMvc.perform(post("/api/v1/advertisements/{id}/click", id))
                .andExpect(status().isOk());

        verify(advertisementService)
                .recordClick(id);
    }
    /**
     * Should record advertisement impression.
     */
    @Test
    void shouldRecordImpression() throws Exception {

        UUID id = UUID.randomUUID();

        doNothing().when(advertisementService)
                .recordImpression(id);

        mockMvc.perform(post("/api/v1/advertisements/{id}/impression", id))
                .andExpect(status().isOk());

        verify(advertisementService)
                .recordImpression(id);
    }
    /**
     * Should return advertisement statistics.
     */
    @Test
    void shouldGetStatistics() throws Exception {

        UUID id = UUID.randomUUID();

        AdvertisementStatisticsResponse response =
                AdvertisementStatisticsResponse.builder()
                        .clicks(20)
                        .impressions(100)
                        .build();

        when(advertisementService.getAdvertisementStatistics(id))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/advertisements/statistics/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clicks").value(20))
                .andExpect(jsonPath("$.impressions").value(100));

        verify(advertisementService)
                .getAdvertisementStatistics(id);
    }

    /**
     * Should return advertiser dashboard.
     */
    @Test
    void shouldGetDashboard() throws Exception {

        AdvertisementDashboardResponse response =
                AdvertisementDashboardResponse.builder()
                        .activeAdvertisements(4)
                        .totalAdvertisements(10)
                        .build();

        when(advertisementService.getDashboard())
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/advertisements/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeAdvertisements")
                        .value(4));

        verify(advertisementService)
                .getDashboard();
    }
}