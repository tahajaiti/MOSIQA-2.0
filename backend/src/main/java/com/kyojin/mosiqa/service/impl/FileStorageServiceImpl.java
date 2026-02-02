package com.kyojin.mosiqa.service.impl;

import com.kyojin.mosiqa.entity.AudioFile;
import com.kyojin.mosiqa.entity.CoverImage;
import com.kyojin.mosiqa.exception.InvalidFileException;
import com.kyojin.mosiqa.repository.AudioFileRepository;
import com.kyojin.mosiqa.repository.CoverImageRepository;
import com.kyojin.mosiqa.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FileStorageServiceImpl implements FileStorageService {

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final List<String> ALLOWED_AUDIO_TYPES = Arrays.asList(
            "audio/mpeg", "audio/wav", "audio/ogg", "audio/mp3", "audio/x-wav"
    );
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/png", "image/jpeg", "image/jpg"
    );

    private final AudioFileRepository audioFileRepository;
    private final CoverImageRepository coverImageRepository;

    @Override
    public AudioFile saveAudioFile(MultipartFile file) {
        validateAudioFile(file);
        
        try {
            AudioFile audioFile = AudioFile.builder()
                    .name(file.getOriginalFilename())
                    .size(file.getSize())
                    .mimeType(file.getContentType())
                    .data(file.getBytes())
                    .build();
            
            AudioFile saved = audioFileRepository.save(audioFile);
            log.info("Saved audio file: {} with id: {}", saved.getName(), saved.getId());
            return saved;
        } catch (IOException e) {
            log.error("Failed to save audio file: {}", e.getMessage());
            throw new InvalidFileException("Failed to save audio file: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AudioFile> getAudioFile(String id) {
        return audioFileRepository.findById(id);
    }

    @Override
    public void deleteAudioFile(String id) {
        if (audioFileRepository.existsById(id)) {
            audioFileRepository.deleteById(id);
            log.info("Deleted audio file with id: {}", id);
        }
    }

    @Override
    public CoverImage saveCoverImage(MultipartFile file) {
        validateImageFile(file);
        
        try {
            CoverImage coverImage = CoverImage.builder()
                    .name(file.getOriginalFilename())
                    .size(file.getSize())
                    .mimeType(file.getContentType())
                    .data(file.getBytes())
                    .build();
            
            CoverImage saved = coverImageRepository.save(coverImage);
            log.info("Saved cover image: {} with id: {}", saved.getName(), saved.getId());
            return saved;
        } catch (IOException e) {
            log.error("Failed to save cover image: {}", e.getMessage());
            throw new InvalidFileException("Failed to save cover image: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CoverImage> getCoverImage(String id) {
        return coverImageRepository.findById(id);
    }

    @Override
    public void deleteCoverImage(String id) {
        if (coverImageRepository.existsById(id)) {
            coverImageRepository.deleteById(id);
            log.info("Deleted cover image with id: {}", id);
        }
    }

    @Override
    public void validateAudioFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("Audio file is required");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileException("Audio file size exceeds maximum allowed size (50MB)");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_AUDIO_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidFileException("Invalid audio format. Allowed formats: MP3, WAV, OGG");
        }
    }

    @Override
    public void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return;
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileException("Image file size exceeds maximum allowed size (50MB)");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidFileException("Invalid image format. Allowed formats: PNG, JPEG");
        }
    }
}
