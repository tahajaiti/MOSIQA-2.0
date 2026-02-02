package com.kyojin.mosiqa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoverImageDTO {

    private String id;
    private String name;
    private Long size;
    private String mimeType;
    private LocalDateTime createdAt;
}
