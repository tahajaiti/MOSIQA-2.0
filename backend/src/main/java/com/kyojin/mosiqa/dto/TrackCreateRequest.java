package com.kyojin.mosiqa.dto;

import com.kyojin.mosiqa.entity.MusicCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackCreateRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Artist is required")
    @Size(max = 255, message = "Artist must not exceed 255 characters")
    private String artist;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Category is required")
    private MusicCategory category;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Double duration;
}
