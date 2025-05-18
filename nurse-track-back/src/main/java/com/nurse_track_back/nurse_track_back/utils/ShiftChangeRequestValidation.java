package com.nurse_track_back.nurse_track_back.utils;

import com.nurse_track_back.nurse_track_back.domain.enums.RequestStatus;
import com.nurse_track_back.nurse_track_back.domain.enums.ShiftStatus;
import com.nurse_track_back.nurse_track_back.domain.models.Shift;
import com.nurse_track_back.nurse_track_back.domain.models.ShiftChangeRequest;
import com.nurse_track_back.nurse_track_back.domain.models.VacationRequest;
import com.nurse_track_back.nurse_track_back.exceptions.InvalidRequestException;
import com.nurse_track_back.nurse_track_back.repositories.ShiftRepository;
import com.nurse_track_back.nurse_track_back.repositories.SupervisorDepartmentRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.shiftChange.CreateShiftChangeRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.shiftChange.UpdateShiftChangeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class ShiftChangeRequestValidation
{
    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private SupervisorDepartmentRepository supervisorDepartmentRepository;

    public void validateShiftChangeCreation(CreateShiftChangeRequest request)
    {
        Shift offeredShift = shiftRepository.findById(request.getOfferedShiftId()).orElseThrow();
        Shift desiredShift = shiftRepository.findById(request.getDesiredShiftId()).orElseThrow();

        //Validar estado
        validateShiftStatus(offeredShift.getStatus());
        validateShiftStatus(desiredShift.getStatus());

        //Validar solapamiento
        validateShiftsOverlapping(offeredShift, desiredShift);

        //Validar antelacion peticion
        validateRequestTime(desiredShift);
    }

    public void validateShiftChangeUpdate(ShiftChangeRequest existingRequest, UpdateShiftChangeRequest newRequest, Long currentUserId)
    {
        validatePendingStatus(existingRequest);
        validateReviewerForApproval(existingRequest, newRequest.getStatus(), currentUserId);
    }

    private void validatePendingStatus(ShiftChangeRequest request)
    {
        if(request.getStatus() != RequestStatus.PENDING)
        {
            throw new InvalidRequestException("Only the PENDING request could be modified");
        }
    }

    private void validateReviewerForApproval(ShiftChangeRequest request, RequestStatus newStatus, Long currentUserId)
    {
        if (newStatus == RequestStatus.APPROVED || newStatus == RequestStatus.REJECTED)
        {
            boolean isValidSupervisor = supervisorDepartmentRepository.existsByNurseIdAndSupervisorId(
                    request.getRequestingNurse().getId(),
                    currentUserId
            );

            if (!isValidSupervisor)
            {
                throw new InvalidRequestException("You are not the supervisor of this nurse's department");
            }
        }
    }

    private void validateShiftStatus(ShiftStatus status)
    {
        if(status != ShiftStatus.SCHEDULED)
        {
            throw new InvalidRequestException("Just SCHEDULED shift can be interchange");
        }
    }

    private void validateShiftsOverlapping(Shift offeredShift, Shift requestedShift)
    {
        LocalTime startTimeOffered = offeredShift.getShiftTemplate().getStartTime();
        LocalTime endTimeOffered = offeredShift.getShiftTemplate().getEndTime();
        LocalTime startTimeRequested = requestedShift.getShiftTemplate().getStartTime();
        LocalTime endTimeRequested = requestedShift.getShiftTemplate().getEndTime();

        if(startTimeOffered.isBefore(endTimeRequested) &&
                startTimeRequested.isBefore(endTimeOffered))
        {
            throw new InvalidRequestException("Shifts must not overlap");
        }
    }

    private void validateRequestTime(Shift requestedShift)
    {
        LocalDate shiftDate = requestedShift.getShiftDate();
        LocalTime startTime = requestedShift.getShiftTemplate().getStartTime();
        LocalDateTime shiftStartDateTime = LocalDateTime.of(shiftDate, startTime);

        // Si ya pasó el plazo de 24 h antes del comienzo, es inválido:
        if ( LocalDateTime.now().isAfter( shiftStartDateTime.minusHours(24) ) )
        {
            throw new InvalidRequestException("Changes require at least 24 hours notice");
        }
    }
}
