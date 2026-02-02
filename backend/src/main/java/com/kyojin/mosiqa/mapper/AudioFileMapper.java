package com.kyojin.mosiqa.mapper;

import com.kyojin.mosiqa.dto.AudioFileDTO;
import com.kyojin.mosiqa.entity.AudioFile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AudioFileMapper {

    AudioFileDTO toDTO(AudioFile audioFile);

    List<AudioFileDTO> toDTOList(List<AudioFile> audioFiles);
}
