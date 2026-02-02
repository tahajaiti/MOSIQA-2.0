package com.kyojin.mosiqa.service;

import com.kyojin.mosiqa.dto.TrackCreateRequest;
import com.kyojin.mosiqa.dto.TrackDTO;
import com.kyojin.mosiqa.dto.TrackUpdateRequest;
import com.kyojin.mosiqa.entity.AudioFile;
import com.kyojin.mosiqa.entity.CoverImage;
import com.kyojin.mosiqa.entity.MusicCategory;
import com.kyojin.mosiqa.entity.Track;
import com.kyojin.mosiqa.exception.ResourceNotFoundException;
import com.kyojin.mosiqa.mapper.TrackMapper;
import com.kyojin.mosiqa.repository.TrackRepository;
import com.kyojin.mosiqa.service.impl.TrackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrackService Tests")
class TrackServiceImplTest {

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private TrackMapper trackMapper;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private TrackServiceImpl trackService;

    private Track sampleTrack;
    private TrackDTO sampleTrackDTO;
    private AudioFile sampleAudioFile;
    private MultipartFile mockAudioFile;

    @BeforeEach
    void setUp() {
        sampleTrack = Track.builder()
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

        sampleAudioFile = AudioFile.builder()
                .id("audio-1")
                .name("test.mp3")
                .size(1024L)
                .mimeType("audio/mpeg")
                .build();

        mockAudioFile = new MockMultipartFile(
                "audioFile",
                "test.mp3",
                "audio/mpeg",
                "test audio content".getBytes()
        );
    }

    @Nested
    @DisplayName("getAllTracks")
    class GetAllTracks {

        @Test
        @DisplayName("should return all tracks ordered by creation date")
        void shouldReturnAllTracks() {
            Track track2 = Track.builder().id("track-2").title("Song 2").build();
            List<Track> tracks = Arrays.asList(sampleTrack, track2);
            List<TrackDTO> trackDTOs = Arrays.asList(sampleTrackDTO, TrackDTO.builder().id("track-2").build());

            when(trackRepository.findAllByOrderByCreatedAtDesc()).thenReturn(tracks);
            when(trackMapper.toDTOList(tracks)).thenReturn(trackDTOs);

            List<TrackDTO> result = trackService.getAllTracks();

            assertThat(result).hasSize(2);
            verify(trackRepository).findAllByOrderByCreatedAtDesc();
        }

        @Test
        @DisplayName("should return empty list when no tracks exist")
        void shouldReturnEmptyList() {
            when(trackRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of());
            when(trackMapper.toDTOList(List.of())).thenReturn(List.of());

            List<TrackDTO> result = trackService.getAllTracks();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getTrackById")
    class GetTrackById {

        @Test
        @DisplayName("should return track when found")
        void shouldReturnTrackWhenFound() {
            when(trackRepository.findById("track-1")).thenReturn(Optional.of(sampleTrack));
            when(trackMapper.toDTO(sampleTrack)).thenReturn(sampleTrackDTO);

            TrackDTO result = trackService.getTrackById("track-1");

            assertThat(result.getId()).isEqualTo("track-1");
            assertThat(result.getTitle()).isEqualTo("Test Song");
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when track not found")
        void shouldThrowWhenNotFound() {
            when(trackRepository.findById("non-existent")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> trackService.getTrackById("non-existent"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Track not found");
        }
    }

    @Nested
    @DisplayName("createTrack")
    class CreateTrack {

        @Test
        @DisplayName("should create track with audio file only")
        void shouldCreateTrackWithAudioOnly() {
            TrackCreateRequest request = TrackCreateRequest.builder()
                    .title("New Song")
                    .artist("New Artist")
                    .category(MusicCategory.ROCK)
                    .duration(200.0)
                    .build();

            Track newTrack = Track.builder()
                    .title("New Song")
                    .artist("New Artist")
                    .category(MusicCategory.ROCK)
                    .duration(200.0)
                    .build();

            when(fileStorageService.saveAudioFile(any())).thenReturn(sampleAudioFile);
            when(trackMapper.toEntity(request)).thenReturn(newTrack);
            when(trackRepository.save(any(Track.class))).thenReturn(sampleTrack);
            when(trackMapper.toDTO(any(Track.class))).thenReturn(sampleTrackDTO);

            TrackDTO result = trackService.createTrack(request, mockAudioFile, null);

            assertThat(result).isNotNull();
            verify(fileStorageService).validateAudioFile(mockAudioFile);
            verify(fileStorageService).saveAudioFile(mockAudioFile);
            verify(trackRepository).save(any(Track.class));
        }

        @Test
        @DisplayName("should create track with audio and cover image")
        void shouldCreateTrackWithCoverImage() {
            TrackCreateRequest request = TrackCreateRequest.builder()
                    .title("New Song")
                    .artist("New Artist")
                    .category(MusicCategory.ROCK)
                    .duration(200.0)
                    .build();

            MultipartFile mockCoverImage = new MockMultipartFile(
                    "coverImage",
                    "cover.jpg",
                    "image/jpeg",
                    "test image content".getBytes()
            );

            CoverImage savedCover = CoverImage.builder().id("cover-2").build();

            Track newTrack = Track.builder()
                    .title("New Song")
                    .artist("New Artist")
                    .category(MusicCategory.ROCK)
                    .duration(200.0)
                    .build();

            when(fileStorageService.saveAudioFile(any())).thenReturn(sampleAudioFile);
            when(fileStorageService.saveCoverImage(any())).thenReturn(savedCover);
            when(trackMapper.toEntity(request)).thenReturn(newTrack);
            when(trackRepository.save(any(Track.class))).thenReturn(sampleTrack);
            when(trackMapper.toDTO(any(Track.class))).thenReturn(sampleTrackDTO);

            TrackDTO result = trackService.createTrack(request, mockAudioFile, mockCoverImage);

            assertThat(result).isNotNull();
            verify(fileStorageService).saveCoverImage(mockCoverImage);
        }
    }

    @Nested
    @DisplayName("deleteTrack")
    class DeleteTrack {

        @Test
        @DisplayName("should delete track and associated files")
        void shouldDeleteTrackAndFiles() {
            when(trackRepository.findById("track-1")).thenReturn(Optional.of(sampleTrack));

            trackService.deleteTrack("track-1");

            verify(fileStorageService).deleteAudioFile("audio-1");
            verify(fileStorageService).deleteCoverImage("cover-1");
            verify(trackRepository).delete(sampleTrack);
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when track not found")
        void shouldThrowWhenNotFound() {
            when(trackRepository.findById("non-existent")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> trackService.deleteTrack("non-existent"))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("searchTracks")
    class SearchTracks {

        @Test
        @DisplayName("should return matching tracks for query")
        void shouldReturnMatchingTracks() {
            List<Track> tracks = List.of(sampleTrack);
            List<TrackDTO> trackDTOs = List.of(sampleTrackDTO);

            when(trackRepository.searchByTitleOrArtist("Test")).thenReturn(tracks);
            when(trackMapper.toDTOList(tracks)).thenReturn(trackDTOs);

            List<TrackDTO> result = trackService.searchTracks("Test");

            assertThat(result).hasSize(1);
            verify(trackRepository).searchByTitleOrArtist("Test");
        }

        @Test
        @DisplayName("should return all tracks when query is empty")
        void shouldReturnAllTracksForEmptyQuery() {
            List<Track> tracks = List.of(sampleTrack);
            List<TrackDTO> trackDTOs = List.of(sampleTrackDTO);

            when(trackRepository.findAllByOrderByCreatedAtDesc()).thenReturn(tracks);
            when(trackMapper.toDTOList(tracks)).thenReturn(trackDTOs);

            List<TrackDTO> result = trackService.searchTracks("");

            verify(trackRepository).findAllByOrderByCreatedAtDesc();
        }
    }
}
