package com.nursetrack.validations.validators;

import com.nursetrack.domain.enums.Status;
import com.nursetrack.utils.ValidationUtils;
import com.nursetrack.validations.annotations.ValidShiftCreation;
import com.nursetrack.web.dto.request.shift.CreateShiftRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.util.List;

public class ShiftCreationValidator implements ConstraintValidator<ValidShiftCreation, CreateShiftRequest> {
    @Override
    public boolean isValid(CreateShiftRequest request, ConstraintValidatorContext context) {
        boolean valid = true;

        // Validar estado cuando hay enfermera asignada
        if (request.getNurseId() != null && !List.of(Status.SCHEDULED, Status.APPROVED).contains(request.getStatus())) {
            ValidationUtils.addValidationError(context, "status", "Nurse can only be assigned to SCHEDULED or APPROVED shifts");
            valid = false;
        }

        // Validar rango de fecha
        if (request.getShiftDate().isAfter(LocalDate.now().plusYears(1))) {
            ValidationUtils.addValidationError(context, "shiftDate", "Shift date cannot be more than 1 year in the future");
            valid = false;
        }

        return valid;
    }
}