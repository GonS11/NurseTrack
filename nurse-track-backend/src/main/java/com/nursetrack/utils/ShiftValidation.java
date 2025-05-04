package com.nursetrack.utils;

import com.nursetrack.domain.enums.ShiftStatus;
import com.nursetrack.domain.model.NurseDepartment;
import com.nursetrack.domain.model.Shift;
import com.nursetrack.exception.AssignmentException;
import com.nursetrack.exception.InvalidStatusException;
import com.nursetrack.exception.UserNotFoundException;
import com.nursetrack.repository.NurseDepartmentRepository;
import com.nursetrack.repository.ShiftRepository;
import com.nursetrack.web.dto.request.shift.UpdateShiftRequest;

public class ShiftValidation
{
    public static void validateShiftUpdate(Shift shift, UpdateShiftRequest request,
                                           NurseDepartmentRepository nurseDepartmentRepository,
                                           ShiftRepository shiftRepository)
    {
        //Validar status
        if (shift.getStatus() == ShiftStatus.COMPLETED)
        {
            throw new InvalidStatusException("Completed shifts cannot be modified");
        }

        if (shift.getStatus() == ShiftStatus.CANCELLED
                && (request.getStatus() == null || request.getStatus() != ShiftStatus.SCHEDULED))
        {
            throw new InvalidStatusException("Cancelled shifts can only be rescheduled to SCHEDULED status");
        }

        //Validar enfermera - departamento
        NurseDepartment nurse = nurseDepartmentRepository.findByNurseId(request.getNurseId())
                .orElseThrow(() -> new UserNotFoundException(request.getNurseId()));

        if (!nurse.getDepartment().getId().equals(request.getDepartmentId()))
        {
            throw new AssignmentException("Nurse does not belong to the specified department");
        }

        //Conflicto de turnos
        boolean hasConflict = shiftRepository.hasNurseShiftConflict(
                request.getNurseId(),
                request.getDepartmentId(),
                request.getShiftDate(),
                shift.getId()
        );

        if (hasConflict)
        {
            throw new AssignmentException("Nurse already has a shift assigned for this date");
        }
    }
}
