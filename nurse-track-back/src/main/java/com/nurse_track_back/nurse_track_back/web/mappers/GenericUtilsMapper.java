package com.nurse_track_back.nurse_track_back.web.mappers;

import com.nurse_track_back.nurse_track_back.domain.models.Department;
import com.nurse_track_back.nurse_track_back.domain.models.Shift;
import com.nurse_track_back.nurse_track_back.domain.models.ShiftTemplate;
import com.nurse_track_back.nurse_track_back.domain.models.User;
import com.nurse_track_back.nurse_track_back.exceptions.ResourceNotFoundException;
import com.nurse_track_back.nurse_track_back.repositories.DepartmentRepository;
import com.nurse_track_back.nurse_track_back.repositories.ShiftRepository;
import com.nurse_track_back.nurse_track_back.repositories.ShiftTemplateRepository;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface GenericUtilsMapper
{
    @Named("userIdToUser")
    static User userIdToUser(Long userId, @Context UserRepository userRepository)
    {
        return userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.create("User", userId));
    }

    @Named("departmentIdToDepartment")
    static Department departmentIdToDepartment(Long departmentId, @Context DepartmentRepository departmentRepository)
    {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> ResourceNotFoundException.create("Department", departmentId));
    }

    @Named("shiftTemplateIdToShiftTemplate")
    static ShiftTemplate shiftTemplateIdToShiftTemplate(Long shiftTemplateId, @Context ShiftTemplateRepository shiftTemplateRepository)
    {
        return shiftTemplateRepository.findById(shiftTemplateId)
                .orElseThrow(() -> ResourceNotFoundException.create("Shift template", shiftTemplateId));
    }

    @Named("shiftIdToShift")
    static Shift shiftIdToShift(Long shiftId, @Context ShiftRepository shiftRepository)
    {
        return shiftRepository.findById(shiftId)
                .orElseThrow(() -> ResourceNotFoundException.create("Shift", shiftId));
    }
}
