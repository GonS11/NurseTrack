package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.ShiftTemplate;
import com.nursetrack.web.dto.response.ShiftTemplateResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShiftTemplateMapper
{
    ShiftTemplateResponse toDto(ShiftTemplate shiftTemplate);

    List<ShiftTemplateResponse> toDtoList(List<ShiftTemplate> shiftTemplates);
}
