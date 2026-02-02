package com.kyojin.mosiqa.controller;

import com.kyojin.mosiqa.dto.TrackCreateRequest;
import com.kyojin.mosiqa.dto.TrackDTO;
import com.kyojin.mosiqa.dto.TrackUpdateRequest;
import com.kyojin.mosiqa.entity.MusicCategory;
import com.kyojin.mosiqa.service.TrackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
@Slf4j
public class TrackController {

    private final TrackService trackService;

    @GetMapping
    public ResponseEntity<List<TrackDTO>> getAllTracks() {
        log.debug("GET /api/tracks - Getting all tracks");
        List<TrackDTO> tracks = trackService.getAllTracks();
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackDTO> getTrackById(@PathVariable String id) {
        log.debug("GET /api/tracks/{} - Getting track by id", id);
        TrackDTO track = trackService.getTrackById(id);
        return ResponseEntity.ok(track);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrackDTO> createTrack(
            @Valid @RequestPart("metadata") TrackCreateRequest request,
            @RequestPart("audioFile") MultipartFile audioFile,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        
        log.debug("POST /api/tracks - Creating new track: {} - {}", request.getTitle(), request.getArtist());
        TrackDTO createdTrack = trackService.createTrack(request, audioFile, coverImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTrack);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TrackDTO> updateTrack(
            @PathVariable String id,
            @Valid @RequestPart("metadata") TrackUpdateRequest request,
            @RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        
        log.debug("PUT /api/tracks/{} - Updating track", id);
        TrackDTO updatedTrack = trackService.updateTrack(id, request, audioFile, coverImage);
        return ResponseEntity.ok(updatedTrack);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable String id) {
        log.debug("DELETE /api/tracks/{} - Deleting track", id);
        trackService.deleteTrack(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrackDTO>> searchTracks(@RequestParam(required = false) String q) {
        log.debug("GET /api/tracks/search?q={} - Searching tracks", q);
        List<TrackDTO> tracks = trackService.searchTracks(q);
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<TrackDTO>> getTracksByCategory(@PathVariable MusicCategory category) {
        log.debug("GET /api/tracks/category/{} - Getting tracks by category", category);
        List<TrackDTO> tracks = trackService.getTracksByCategory(category);
        return ResponseEntity.ok(tracks);
    }
}
