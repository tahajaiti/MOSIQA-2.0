package com.kyojin.mosiqa.controller;

import com.kyojin.mosiqa.entity.AudioFile;
import com.kyojin.mosiqa.entity.CoverImage;
import com.kyojin.mosiqa.exception.ResourceNotFoundException;
import com.kyojin.mosiqa.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/audio/{id}")
    public ResponseEntity<byte[]> getAudioFile(@PathVariable String id) {
        log.debug("GET /api/files/audio/{} - Streaming audio file", id);
        
        AudioFile audioFile = fileStorageService.getAudioFile(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audio file", "id", id));
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(audioFile.getMimeType()));
        headers.setContentLength(audioFile.getSize());
        headers.set(HttpHeaders.CONTENT_DISPOSITION, 
                "inline; filename=\"" + audioFile.getName() + "\"");
        
        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        
        return new ResponseEntity<>(audioFile.getData(), headers, HttpStatus.OK);
    }

    @GetMapping("/cover/{id}")
    public ResponseEntity<byte[]> getCoverImage(@PathVariable String id) {
        log.debug("GET /api/files/cover/{} - Getting cover image", id);
        
        CoverImage coverImage = fileStorageService.getCoverImage(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cover image", "id", id));
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(coverImage.getMimeType()));
        headers.setContentLength(coverImage.getSize());
        headers.setCacheControl("max-age=31536000");
        
        return new ResponseEntity<>(coverImage.getData(), headers, HttpStatus.OK);
    }
}
