package com.kyojin.mosiqa.service;

import com.kyojin.mosiqa.entity.AudioFile;
import com.kyojin.mosiqa.entity.CoverImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileStorageService {

    AudioFile saveAudioFile(MultipartFile file);

    Optional<AudioFile> getAudioFile(String id);

    void deleteAudioFile(String id);

    CoverImage saveCoverImage(MultipartFile file);

    Optional<CoverImage> getCoverImage(String id);

    void deleteCoverImage(String id);

    void validateAudioFile(MultipartFile file);

    void validateImageFile(MultipartFile file);
}
