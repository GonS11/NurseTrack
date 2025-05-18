package com.nurse_track_back.nurse_track_back.validations.validators;

import com.nurse_track_back.nurse_track_back.domain.enums.ShiftStatus;
import com.nurse_track_back.nurse_track_back.utils.ValidationUtils;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidShiftRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.shift.CreateShiftRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.shift.UpdateShiftRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.util.List;

public class ShiftRequestValidator implements ConstraintValidator<ValidShiftRequest, Object>
{
    @Override
    public boolean isValid(Object request, ConstraintValidatorContext context)
    {
        if (request instanceof CreateShiftRequest createReq)
        {
            return validateCreation(createReq, context);
        }
        else if (request instanceof UpdateShiftRequest updateReq)
        {
            return validateUpdate(updateReq, context);
        }

        return true;
    }

    private boolean validateCreation(CreateShiftRequest request, ConstraintValidatorContext context)
    {
        boolean valid = true;

        // Validar estado cuando hay enfermera asignada
        if (request.getNurseId() != null
                && !List.of(ShiftStatus.SCHEDULED, ShiftStatus.SWAPPED).contains(request.getStatus()))
        {
            ValidationUtils.addValidationError(context, "status",
                                               "Nurse can only be assigned to SCHEDULED or APPROVED shifts");
            valid = false;
        }

        // Validar rango de fecha
        if (request.getShiftDate().isAfter(LocalDate.now().plusYears(1)))
        {
            ValidationUtils.addValidationError(context, "shiftDate",
                                               "Shift date cannot be more than 1 year in the future");
            valid = false;
        }

        return valid;
    }

    private boolean validateUpdate(UpdateShiftRequest request, ConstraintValidatorContext context)
    {
        boolean isValid = true;

        // Validar nurseId y status
        if (request.getNurseId() != null
                && request.getStatus() != null
                && !List.of(ShiftStatus.SCHEDULED, ShiftStatus.SWAPPED).contains(request.getStatus()))
        {
            ValidationUtils.addValidationError(context, "status",
                                               "Shifts with assigned nurses must be SCHEDULED or SWAPPED");
            isValid = false;
        }

        // Validar fecha
        if (request.getShiftDate() != null
                && request.getShiftDate().isAfter(LocalDate.now().plusYears(1)))
        {
            ValidationUtils.addValidationError(context, "shiftDate",
                                               "Shift date cannot be more than 1 year in the future");
            isValid = false;
        }

        return isValid;
    }
}
