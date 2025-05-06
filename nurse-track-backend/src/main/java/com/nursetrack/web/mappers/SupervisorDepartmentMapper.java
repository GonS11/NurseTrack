package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.SupervisorDepartment;
import com.nursetrack.repository.DepartmentRepository;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.web.dto.request.supervisorDepartment.AssignSupervisorRequest;
import com.nursetrack.web.dto.response.SupervisorDepartmentResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, UserMapper.class})
public interface SupervisorDepartmentMapper
{
    // 1. Entity → Response
    @Mapping(source = "supervisor", target = "supervisor", qualifiedByName = "userToSimpleResponse")
    @Mapping(source = "department", target = "department")
    SupervisorDepartmentResponse toDto(SupervisorDepartment entity);
    // 2. Versión básica para creación (sin resolución de dependencias)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedAt", ignore = true)
    SupervisorDepartment toEntity(AssignSupervisorRequest request);

    @AfterMapping
    default void resolveAssignRelations (AssignSupervisorRequest request, @MappingTarget SupervisorDepartment supervisorDepartment,
                                         @Context UserRepository userRepository,
                                         @Context DepartmentRepository departmentRepository)
    {
        supervisorDepartment.setSupervisor(userRepository.getReferenceById(request.getSupervisorId()));
        supervisorDepartment.setDepartment(departmentRepository.getReferenceById(request.getDepartmentId()));
    }

    // 3. Lista de Entities → Lista de Responses
    List<SupervisorDepartmentResponse> toDtoList(List<SupervisorDepartment> supervisorDepartmentList);
}