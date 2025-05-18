package com.nurse_track_back.nurse_track_back.web.mappers;

import com.nurse_track_back.nurse_track_back.domain.models.Shift;
import com.nurse_track_back.nurse_track_back.repositories.DepartmentRepository;
import com.nurse_track_back.nurse_track_back.repositories.ShiftTemplateRepository;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.shift.CreateShiftRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.shift.UpdateShiftRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.ShiftResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, DepartmentMapper.class, ShiftTemplateMapper.class, GenericUtilsMapper.class})
public interface ShiftMapper
{
    @Mapping(source = "nurse", target = "nurse", qualifiedByName = "userToSimpleResponse")
    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "userToSimpleResponse")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "shiftTemplate", target = "shiftTemplate")
    ShiftResponse toDTO(Shift shift);

    List<ShiftResponse> toDTOList(List<Shift> allByDepartmentId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nurse", source = "nurseId", qualifiedByName = "userIdToUser")  // Se maneja en el servicio
    @Mapping(target = "department", source = "departmentId", qualifiedByName = "departmentIdToDepartment")
    @Mapping(target = "shiftTemplate", source = "shiftTemplateId", qualifiedByName = "shiftTemplateIdToShiftTemplate")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdBy", source = "createdById", qualifiedByName = "userIdToUser")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "offeredInShiftChanges", ignore = true)
    @Mapping(target = "desiredInShiftChanges", ignore = true)
    Shift toEntity(CreateShiftRequest request, @Context UserRepository userRepository,
                   @Context DepartmentRepository departmentRepository, @Context ShiftTemplateRepository shiftTemplateRepository);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nurse", source = "nurseId", qualifiedByName = "userIdToUser")  // Se maneja en el servicio
    @Mapping(target = "department", source = "departmentId", qualifiedByName = "departmentIdToDepartment")
    @Mapping(target = "shiftTemplate", source = "shiftTemplateId", qualifiedByName = "shiftTemplateIdToShiftTemplate")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "offeredInShiftChanges", ignore = true)
    @Mapping(target = "desiredInShiftChanges", ignore = true)
    void updateModel(UpdateShiftRequest request, @MappingTarget Shift shift,
                     @Context UserRepository userRepository, @Context DepartmentRepository departmentRepository,
                     @Context ShiftTemplateRepository shiftTemplateRepository);
}
