package com.nursetrack.utils;

import com.nursetrack.domain.enums.ShiftStatus;
import com.nursetrack.domain.model.Shift;
import com.nursetrack.domain.model.ShiftChangeRequest;
import com.nursetrack.exception.ShiftChangeRequestException;
import com.nursetrack.repository.ShiftRepository;
import com.nursetrack.web.dto.request.shiftChange.CreateShiftChangeRequest;
import com.nursetrack.web.dto.request.shiftChange.UpdateShiftChangeRequest;
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

    public void validateShiftChangeUpdate(ShiftChangeRequest shiftChangeRequest, UpdateShiftChangeRequest request)
    {

    }

    private void validateShiftStatus(ShiftStatus status)
    {
        if(status != ShiftStatus.SCHEDULED)
        {
            throw new ShiftChangeRequestException("Just the scheluded shift can be interchange");
        }
    }

    private void validateShiftsOverlapping(Shift offeredShift, Shift requestedShift)
    {
        LocalTime startTimeOffered = offeredShift.getShiftTemplate().getShiftStartTime();
        LocalTime endTimeOffered = offeredShift.getShiftTemplate().getShiftEndTime();
        LocalTime startTimeRequested = requestedShift.getShiftTemplate().getShiftStartTime();
        LocalTime endTimeRequested = requestedShift.getShiftTemplate().getShiftEndTime();

        if(startTimeOffered.isBefore(endTimeRequested) &&
                startTimeRequested.isBefore(endTimeOffered))
        {
            throw new ShiftChangeRequestException("Shifts must not overlap");
        }
    }

    private void validateRequestTime(Shift requestedShift)
    {
        LocalDate shiftDate = requestedShift.getShiftDate();
        LocalTime startTime = requestedShift.getShiftTemplate().getShiftStartTime();
        LocalDateTime shiftStartDateTime = LocalDateTime.of(shiftDate, startTime);

        if(LocalDateTime.now().isBefore(shiftStartDateTime.minusHours(24)))
        {
            throw new ShiftChangeRequestException("Changes require at least 24 hours notice");
        }
    }
}
