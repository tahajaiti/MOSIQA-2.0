package com.kyojin.mosiqa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyojin.mosiqa.dto.TrackCreateRequest;
import com.kyojin.mosiqa.dto.TrackDTO;
import com.kyojin.mosiqa.dto.TrackUpdateRequest;
import com.kyojin.mosiqa.entity.MusicCategory;
import com.kyojin.mosiqa.exception.GlobalExceptionHandler;
import com.kyojin.mosiqa.exception.ResourceNotFoundException;
import com.kyojin.mosiqa.service.TrackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrackController Tests")
class TrackControllerTest {

    @Mock
    private TrackService trackService;

    @InjectMocks
    private TrackController trackController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private TrackDTO sampleTrackDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trackController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        sampleTrackDTO = TrackDTO.builder()
                .id("track-1")
                .title("Test Song")
                .artist("Test Artist")
                .description("Test Description")
                .category(MusicCategory.POP)
                .duration(180.0)
                .audioFileId("audio-1")
                .coverImageId("cover-1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("GET /api/tracks")
    class GetAllTracks {

        @Test
        @DisplayName("should return all tracks")
        void shouldReturnAllTracks() throws Exception {
            TrackDTO track2 = TrackDTO.builder().id("track-2").title("Song 2").build();
            List<TrackDTO> tracks = Arrays.asList(sampleTrackDTO, track2);

            when(trackService.getAllTracks()).thenReturn(tracks);

            mockMvc.perform(get("/api/tracks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").value("track-1"))
                    .andExpect(jsonPath("$[0].title").value("Test Song"));
        }

        @Test
        @DisplayName("should return empty list when no tracks")
        void shouldReturnEmptyList() throws Exception {
            when(trackService.getAllTracks()).thenReturn(List.of());

            mockMvc.perform(get("/api/tracks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/tracks/{id}")
    class GetTrackById {

        @Test
        @DisplayName("should return track when found")
        void shouldReturnTrackWhenFound() throws Exception {
            when(trackService.getTrackById("track-1")).thenReturn(sampleTrackDTO);

            mockMvc.perform(get("/api/tracks/track-1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("track-1"))
                    .andExpect(jsonPath("$.title").value("Test Song"))
                    .andExpect(jsonPath("$.artist").value("Test Artist"));
        }

        @Test
        @DisplayName("should return 404 when track not found")
        void shouldReturn404WhenNotFound() throws Exception {
            when(trackService.getTrackById("non-existent"))
                    .thenThrow(new ResourceNotFoundException("Track", "id", "non-existent"));

            mockMvc.perform(get("/api/tracks/non-existent"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(containsString("Track not found")));
        }
    }

    @Nested
    @DisplayName("POST /api/tracks")
    class CreateTrack {

        @Test
        @DisplayName("should create track successfully")
        void shouldCreateTrack() throws Exception {
            TrackCreateRequest request = TrackCreateRequest.builder()
                    .title("New Song")
                    .artist("New Artist")
                    .category(MusicCategory.ROCK)
                    .duration(200.0)
                    .build();

            MockMultipartFile metadataFile = new MockMultipartFile(
                    "metadata",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(request)
            );

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audioFile",
                    "test.mp3",
                    "audio/mpeg",
                    "test content".getBytes()
            );

            when(trackService.createTrack(any(), any(), any())).thenReturn(sampleTrackDTO);

            mockMvc.perform(multipart("/api/tracks")
                            .file(metadataFile)
                            .file(audioFile))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value("track-1"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/tracks/{id}")
    class DeleteTrack {

        @Test
        @DisplayName("should delete track successfully")
        void shouldDeleteTrack() throws Exception {
            doNothing().when(trackService).deleteTrack("track-1");

            mockMvc.perform(delete("/api/tracks/track-1"))
                    .andExpect(status().isNoContent());

            verify(trackService).deleteTrack("track-1");
        }

        @Test
        @DisplayName("should return 404 when track not found")
        void shouldReturn404WhenNotFound() throws Exception {
            doThrow(new ResourceNotFoundException("Track", "id", "non-existent"))
                    .when(trackService).deleteTrack("non-existent");

            mockMvc.perform(delete("/api/tracks/non-existent"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/tracks/search")
    class SearchTracks {

        @Test
        @DisplayName("should return matching tracks")
        void shouldReturnMatchingTracks() throws Exception {
            List<TrackDTO> tracks = List.of(sampleTrackDTO);
            when(trackService.searchTracks("Test")).thenReturn(tracks);

            mockMvc.perform(get("/api/tracks/search").param("q", "Test"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].title").value("Test Song"));
        }
    }

    @Nested
    @DisplayName("GET /api/tracks/category/{category}")
    class GetTracksByCategory {

        @Test
        @DisplayName("should return tracks by category")
        void shouldReturnTracksByCategory() throws Exception {
            List<TrackDTO> tracks = List.of(sampleTrackDTO);
            when(trackService.getTracksByCategory(MusicCategory.POP)).thenReturn(tracks);

            mockMvc.perform(get("/api/tracks/category/POP"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].category").value("POP"));
        }
    }
}
