package com.nurse_track_back.nurse_track_back.utils;

import com.nurse_track_back.nurse_track_back.domain.models.Shift;
import com.nurse_track_back.nurse_track_back.exceptions.SecurityException;
import com.nurse_track_back.nurse_track_back.repositories.SupervisorDepartmentRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShiftAuthorizationValidator
{
    private final SupervisorDepartmentRepository supervisorDepartmentRepo;

    public void validateSupervisorShiftAccess(Long supervisorId,
                                              Shift shift,
                                              @Nullable Long targetDepartmentId)
    {
        validateCurrentDepartmentAccess(supervisorId, shift);

        if (targetDepartmentId != null)
        {
            validateTargetDepartmentAccess(supervisorId, targetDepartmentId, shift);
        }
    }

    private void validateCurrentDepartmentAccess(Long supervisorId, Shift shift)
    {
        if (!supervisorDepartmentRepo.existsBySupervisorIdAndDepartmentId(supervisorId,shift.getDepartment().getId()))
        {
            throw SecurityException.forbidden(supervisorId,"actual shift department with id " + shift.getId());
        }
    }

    private void validateTargetDepartmentAccess(Long supervisorId, Long targetDepartmentId, Shift shift)
    {
        if (!targetDepartmentId.equals(shift.getDepartment().getId()) &&
                !supervisorDepartmentRepo.existsBySupervisorIdAndDepartmentId(supervisorId, targetDepartmentId))
        {
            throw SecurityException.forbidden(supervisorId, "actual department with id " + targetDepartmentId);
        }
    }
}