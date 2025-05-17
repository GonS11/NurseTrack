package com.nurse_track_back.nurse_track_back.web.mappers;

import com.nurse_track_back.nurse_track_back.domain.models.ShiftTemplate;
import com.nurse_track_back.nurse_track_back.web.dto.response.ShiftTemplateResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShiftTemplateMapper
{
    ShiftTemplateResponse toDTO(ShiftTemplate shiftTemplate);

    List<ShiftTemplateResponse> toDTOList(List<ShiftTemplate> shiftTemplates);
}
