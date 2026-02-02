package com.kyojin.mosiqa.dto;

import com.kyojin.mosiqa.entity.MusicCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackDTO {

    private String id;
    private String title;
    private String artist;
    private String description;
    private MusicCategory category;
    private Double duration;
    private String audioFileId;
    private String coverImageId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
