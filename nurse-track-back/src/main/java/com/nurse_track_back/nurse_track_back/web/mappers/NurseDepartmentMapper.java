package com.nurse_track_back.nurse_track_back.web.mappers;

import com.nurse_track_back.nurse_track_back.domain.models.NurseDepartment;
import com.nurse_track_back.nurse_track_back.repositories.DepartmentRepository;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.nurseDepartment.AssignNurseRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.NurseDepartmentResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { DepartmentMapper.class, UserMapper.class, GenericUtilsMapper.class })
public interface NurseDepartmentMapper {
    // Entity → Response
    @Mapping(source = "nurse", target = "nurse", qualifiedByName = "userToSimpleResponse")
    @Mapping(source = "department", target = "department")
    NurseDepartmentResponse toDTO(NurseDepartment entity);

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nurse", source = "nurseId", qualifiedByName = "userIdToUser")
    @Mapping(target = "department", source = "departmentId", qualifiedByName = "departmentIdToDepartment")
    @Mapping(target = "assignedAt", ignore = true)
    NurseDepartment toEntity(AssignNurseRequest request, @Context UserRepository userRepository,
            @Context DepartmentRepository departmentRepository);

    // 3. Lista de Entities → Lista de Responses
    List<NurseDepartmentResponse> toDtoList(List<NurseDepartment> nurseDepartmentList);
}
