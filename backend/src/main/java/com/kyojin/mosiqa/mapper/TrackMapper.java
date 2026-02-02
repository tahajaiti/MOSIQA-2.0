package com.kyojin.mosiqa.mapper;

import com.kyojin.mosiqa.dto.TrackCreateRequest;
import com.kyojin.mosiqa.dto.TrackDTO;
import com.kyojin.mosiqa.dto.TrackUpdateRequest;
import com.kyojin.mosiqa.entity.Track;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrackMapper {

    TrackDTO toDTO(Track track);

    List<TrackDTO> toDTOList(List<Track> tracks);

    Track toEntity(TrackCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(TrackUpdateRequest request, @MappingTarget Track track);
}
