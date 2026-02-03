package com.kyojin.mosiqa.service.impl;

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
import com.kyojin.mosiqa.service.FileStorageService;
import com.kyojin.mosiqa.service.TrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional(readOnly = true)
    public List<TrackDTO> getAllTracks() {
        List<Track> tracks = trackRepository.findAllByOrderByCreatedAtDesc();
        return trackMapper.toDTOList(tracks);
    }

    @Override
    @Transactional(readOnly = true)
    public TrackDTO getTrackById(String id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Track", "id", id));
        return trackMapper.toDTO(track);
    }

    @Override
    public TrackDTO createTrack(TrackCreateRequest request, MultipartFile audioFile, MultipartFile coverImage) {
        fileStorageService.validateAudioFile(audioFile);
        AudioFile savedAudio = fileStorageService.saveAudioFile(audioFile);
        
        String coverImageId = null;
        if (coverImage != null && !coverImage.isEmpty()) {
            fileStorageService.validateImageFile(coverImage);
            CoverImage savedCover = fileStorageService.saveCoverImage(coverImage);
            coverImageId = savedCover.getId();
        }
        
        Track track = trackMapper.toEntity(request);
        track.setAudioFileId(savedAudio.getId());
        track.setCoverImageId(coverImageId);
        
        // Set default duration if not provided
        if (track.getDuration() == null || track.getDuration() <= 0) {
            track.setDuration(0.0);
        }
        
        Track savedTrack = trackRepository.save(track);
        log.info("Created track: {} - {} with id: {}", savedTrack.getTitle(), savedTrack.getArtist(), savedTrack.getId());
        
        return trackMapper.toDTO(savedTrack);
    }

    @Override
    public TrackDTO updateTrack(String id, TrackUpdateRequest request, MultipartFile audioFile, MultipartFile coverImage) {
        Track existingTrack = trackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Track", "id", id));
        
        trackMapper.updateEntityFromRequest(request, existingTrack);
        
        if (audioFile != null && !audioFile.isEmpty()) {
            fileStorageService.validateAudioFile(audioFile);
            
            if (existingTrack.getAudioFileId() != null) {
                fileStorageService.deleteAudioFile(existingTrack.getAudioFileId());
            }
            
            AudioFile savedAudio = fileStorageService.saveAudioFile(audioFile);
            existingTrack.setAudioFileId(savedAudio.getId());
        }
        
        if (coverImage != null && !coverImage.isEmpty()) {
            fileStorageService.validateImageFile(coverImage);
            
            if (existingTrack.getCoverImageId() != null) {
                fileStorageService.deleteCoverImage(existingTrack.getCoverImageId());
            }
            
            CoverImage savedCover = fileStorageService.saveCoverImage(coverImage);
            existingTrack.setCoverImageId(savedCover.getId());
        }
        
        Track updatedTrack = trackRepository.save(existingTrack);
        log.info("Updated track with id: {}", id);
        
        return trackMapper.toDTO(updatedTrack);
    }

    @Override
    public void deleteTrack(String id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Track", "id", id));
        
        if (track.getAudioFileId() != null) {
            fileStorageService.deleteAudioFile(track.getAudioFileId());
        }
        if (track.getCoverImageId() != null) {
            fileStorageService.deleteCoverImage(track.getCoverImageId());
        }
        
        trackRepository.delete(track);
        log.info("Deleted track with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackDTO> searchTracks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllTracks();
        }
        List<Track> tracks = trackRepository.searchByTitleOrArtist(query.trim());
        return trackMapper.toDTOList(tracks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackDTO> getTracksByCategory(MusicCategory category) {
        List<Track> tracks = trackRepository.findByCategory(category);
        return trackMapper.toDTOList(tracks);
    }
}
