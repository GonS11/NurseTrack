package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.NurseDepartment;
import com.nursetrack.web.dto.request.nurseDepartment.AssignNurseRequest;
import com.nursetrack.web.dto.response.NurseDepartmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, UserMapper.class})
public interface NurseDepartmentMapper
{
    // Entity → Response
    @Mapping(source = "nurse", target = "nurse") // Mapeo automático a NurseInfo
    @Mapping(source = "department", target = "department")
    NurseDepartmentResponse toDTO(NurseDepartment entity);

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedAt", ignore = true)
    @Mapping(target = "nurse",ignore = true)
    @Mapping(target = "department",ignore = true)
    NurseDepartment toModel(AssignNurseRequest request);

    // 3. Lista de Entities → Lista de Responses
    List<NurseDepartmentResponse> toDtoList(List<NurseDepartment> nurseDepartmentList);
}