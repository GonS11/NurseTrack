package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.Shift;
import com.nursetrack.repository.DepartmentRepository;
import com.nursetrack.repository.ShiftTemplateRepository;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.web.dto.request.shift.CreateShiftRequest;
import com.nursetrack.web.dto.request.shift.UpdateShiftRequest;
import com.nursetrack.web.dto.response.ShiftResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, DepartmentMapper.class, ShiftTemplateMapper.class})
public interface ShiftMapper
{
    ShiftResponse toDTO(Shift shift);

    List<ShiftResponse> toDTOList(List<Shift> allByDepartmentId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)  // Se maneja en el servicio
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Shift toEntity(CreateShiftRequest request);

    @AfterMapping
    default void resolveCreateRelations(CreateShiftRequest request, @MappingTarget Shift shift,
                                  @Context UserRepository userRepository,
                                  @Context DepartmentRepository departmentRepository,
                                  @Context ShiftTemplateRepository shiftTemplateRepository)
    {
        //Asignaciones FK, solo con reference no select completo pq ya esta validado si existe
        if(request.getNurseId() != null) //Puede ser null
        {
            shift.setNurse(userRepository.getReferenceById(request.getNurseId()));
        }
        shift.setDepartment(departmentRepository.getReferenceById(request.getDepartmentId()));
        shift.setShiftTemplate(shiftTemplateRepository.getReferenceById(request.getShiftTemplateId()));
        shift.setCreatedBy(userRepository.getReferenceById(request.getCreatedById()));
    }

    void updateModel(UpdateShiftRequest request, @MappingTarget Shift shift);

    @AfterMapping
    default void resolveUpdateRelations(UpdateShiftRequest request, @MappingTarget Shift shift,
                                        @Context UserRepository userRepository,
                                        @Context DepartmentRepository departmentRepository,
                                        @Context ShiftTemplateRepository shiftTemplateRepository)
    {
        //Asignaciones FK, solo con reference no select completo pq ya esta validado si existe
        if(request.getNurseId() != null) //Puede ser null
        {
            shift.setNurse(userRepository.getReferenceById(request.getNurseId()));
        }
        shift.setDepartment(departmentRepository.getReferenceById(request.getDepartmentId()));
        shift.setShiftTemplate(shiftTemplateRepository.getReferenceById(request.getShiftTemplateId()));
    }
}