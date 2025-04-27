package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.SupervisorDepartment;
import com.nursetrack.web.dto.request.supervisorDepartment.AssignSupervisorRequest;
import com.nursetrack.web.dto.response.SupervisorDepartmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, UserMapper.class})
public interface SupervisorDepartmentMapper
{
    // 1. Entity → Response
    @Mapping(source = "supervisor", target = "supervisor")
    @Mapping(source = "department", target = "department")
    SupervisorDepartmentResponse toDto(SupervisorDepartment entity);

    // 2. Versión básica para creación (sin resolución de dependencias)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedAt", ignore = true)
    @Mapping(target = "supervisor", ignore = true) // Se asignará manualmente
    @Mapping(target = "department", ignore = true) // Se asignará manualmente
    SupervisorDepartment toModel(AssignSupervisorRequest request);

    // 3. Lista de Entities → Lista de Responses
    List<SupervisorDepartmentResponse> toDtoList(List<SupervisorDepartment> supervisorDepartmentList);
}