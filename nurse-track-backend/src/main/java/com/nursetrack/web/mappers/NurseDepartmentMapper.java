package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.NurseDepartment;
import com.nursetrack.repository.DepartmentRepository;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.web.dto.request.nurseDepartment.AssignNurseRequest;
import com.nursetrack.web.dto.response.NurseDepartmentResponse;
import org.mapstruct.*;

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
    NurseDepartment toEntity(AssignNurseRequest request);

    @AfterMapping
    default void resolveAssignRelations (AssignNurseRequest request, @MappingTarget NurseDepartment nurseDepartment,
                                         @Context UserRepository userRepository,
                                         @Context DepartmentRepository departmentRepository)
    {
        nurseDepartment.setNurse(userRepository.getReferenceById(request.getNurseId()));
        nurseDepartment.setDepartment(departmentRepository.getReferenceById(request.getDepartmentId()));
    }

    // 3. Lista de Entities → Lista de Responses
    List<NurseDepartmentResponse> toDtoList(List<NurseDepartment> nurseDepartmentList);
}