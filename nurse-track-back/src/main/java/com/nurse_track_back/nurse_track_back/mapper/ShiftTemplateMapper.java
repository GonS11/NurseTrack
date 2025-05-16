package com.nurse_track_back.nurse_track_back.mapper;

import com.nurse_track_back.nurse_track_back.domain.model.ShiftTemplate;
import com.nurse_track_back.nurse_track_back.dto.response.ShiftTemplateResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShiftTemplateMapper
{
    ShiftTemplateResponse toDTO(ShiftTemplate shiftTemplate);

    List<ShiftTemplateResponse> toDTOList(List<ShiftTemplate> shiftTemplates);
}
