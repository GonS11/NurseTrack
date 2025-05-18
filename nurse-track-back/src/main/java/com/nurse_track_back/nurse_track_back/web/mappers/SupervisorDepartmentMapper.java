package com.nurse_track_back.nurse_track_back.web.mappers;

import com.nurse_track_back.nurse_track_back.domain.models.SupervisorDepartment;
import com.nurse_track_back.nurse_track_back.repositories.DepartmentRepository;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.supervisorDepartment.AssignSupervisorRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.SupervisorDepartmentResponse;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {DepartmentMapper.class, UserMapper.class, GenericUtilsMapper.class})
public interface SupervisorDepartmentMapper
{
    // 1. Entity → Response
    @Mapping(source = "supervisor", target = "supervisor", qualifiedByName = "userToSimpleResponse")
    @Mapping(source = "department", target = "department")
    SupervisorDepartmentResponse toDTO(SupervisorDepartment entity);

    // 2. Versión básica para creación (sin resolución de dependencias)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supervisor", source = "supervisorId", qualifiedByName = "userIdToUser")
    @Mapping(target = "department", source = "departmentId", qualifiedByName = "departmentIdToDepartment")
    @Mapping(target = "assignedAt", ignore = true)
    SupervisorDepartment toEntity(AssignSupervisorRequest request, @Context UserRepository userRepository,
                                  @Context DepartmentRepository departmentRepository);

    // 3. Lista de Entities → Lista de Responses
    List<SupervisorDepartmentResponse> toDtoList(List<SupervisorDepartment> supervisorDepartmentList);
}