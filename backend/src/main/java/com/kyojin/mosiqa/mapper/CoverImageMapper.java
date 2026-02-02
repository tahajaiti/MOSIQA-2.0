package com.kyojin.mosiqa.mapper;

import com.kyojin.mosiqa.dto.CoverImageDTO;
import com.kyojin.mosiqa.entity.CoverImage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CoverImageMapper {

    CoverImageDTO toDTO(CoverImage coverImage);

    List<CoverImageDTO> toDTOList(List<CoverImage> coverImages);
}
