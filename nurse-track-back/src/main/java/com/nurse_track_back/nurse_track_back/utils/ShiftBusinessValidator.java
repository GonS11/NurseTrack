package com.nurse_track_back.nurse_track_back.utils;

import com.nurse_track_back.nurse_track_back.domain.enums.ShiftStatus;
import com.nurse_track_back.nurse_track_back.domain.models.Shift;
import com.nurse_track_back.nurse_track_back.exceptions.AssignmentException;
import com.nurse_track_back.nurse_track_back.exceptions.InvalidStatusException;
import com.nurse_track_back.nurse_track_back.repositories.NurseDepartmentRepository;
import com.nurse_track_back.nurse_track_back.repositories.ShiftRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.shift.UpdateShiftRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ShiftBusinessValidator
{

    private final NurseDepartmentRepository nurseDepartmentRepo;
    private final ShiftRepository shiftRepo;

    public void validateUpdate(Shift shift, UpdateShiftRequest request)
    {
        validateStatusTransitions(shift, request);
        validateNurseAssignment(shift, request);
        checkShiftConflicts(shift, request);
    }

    private void validateStatusTransitions(Shift shift, UpdateShiftRequest request)
    {
        if (shift.getStatus() == ShiftStatus.COMPLETED)
        {
            throw new InvalidStatusException("COMPLETED shifts can not be modified");
        }

        if (shift.getStatus() == ShiftStatus.CANCELLED && !isRescheduling(request))
        {
            throw new InvalidStatusException("CANCELED shifts only can be rescheduled");
        }
    }

    private boolean isRescheduling(UpdateShiftRequest request)
    {
        return request.getStatus() == ShiftStatus.SCHEDULED &&
                request.getShiftDate() != null;
    }

    private void validateNurseAssignment(Shift shift, UpdateShiftRequest request)
    {
        final Long targetNurseId = resolveTargetNurseId(shift, request);
        final Long targetDepartmentId = resolveTargetDepartmentId(shift, request);

        if (!nurseDepartmentRepo.existsByNurse_IdAndDepartment_Id(targetNurseId, targetDepartmentId))
        {
            throw AssignmentException.create("Nurse", targetNurseId, targetDepartmentId);
        }
    }

    private void checkShiftConflicts(Shift shift, UpdateShiftRequest request)
    {
        final Long targetNurseId = resolveTargetNurseId(shift, request);
        final Long targetDepartmentId = resolveTargetDepartmentId(shift, request);
        final LocalDate targetDate = resolveTargetDate(shift, request);

        if (shiftRepo.hasNurseShiftConflict(targetNurseId, targetDepartmentId, targetDate, shift.getId()))
        {
            throw new AssignmentException("Shift conflict for the nurse with id " + targetNurseId);
        }
    }

    // Helpers para resolución de valores objetivo
    private Long resolveTargetNurseId(Shift shift, UpdateShiftRequest request)
    {
        return request.getNurseId() != null
                ? request.getNurseId()
                : shift.getNurse().getId();
    }

    private Long resolveTargetDepartmentId(Shift shift, UpdateShiftRequest request)
    {
        return request.getDepartmentId() != null
                ? request.getDepartmentId()
                : shift.getDepartment().getId();
    }

    private LocalDate resolveTargetDate(Shift shift, UpdateShiftRequest request)
    {
        return request.getShiftDate() != null
                ? request.getShiftDate()
                : shift.getShiftDate();
    }
}