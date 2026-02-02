package com.kyojin.mosiqa.service;

import com.kyojin.mosiqa.dto.TrackCreateRequest;
import com.kyojin.mosiqa.dto.TrackDTO;
import com.kyojin.mosiqa.dto.TrackUpdateRequest;
import com.kyojin.mosiqa.entity.MusicCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrackService {

    List<TrackDTO> getAllTracks();

    TrackDTO getTrackById(String id);

    TrackDTO createTrack(TrackCreateRequest request, MultipartFile audioFile, MultipartFile coverImage);

    TrackDTO updateTrack(String id, TrackUpdateRequest request, MultipartFile audioFile, MultipartFile coverImage);

    void deleteTrack(String id);

    List<TrackDTO> searchTracks(String query);

    List<TrackDTO> getTracksByCategory(MusicCategory category);
}
