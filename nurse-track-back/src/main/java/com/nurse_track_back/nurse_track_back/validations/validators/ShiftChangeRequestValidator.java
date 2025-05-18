package com.nurse_track_back.nurse_track_back.validations.validators;

import com.nurse_track_back.nurse_track_back.domain.models.Shift;
import com.nurse_track_back.nurse_track_back.repositories.ShiftRepository;
import com.nurse_track_back.nurse_track_back.utils.ValidationUtils;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidShiftChangeRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.shiftChange.CreateShiftChangeRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ShiftChangeRequestValidator
        implements ConstraintValidator<ValidShiftChangeRequest, CreateShiftChangeRequest> {

    @Autowired
    private ShiftRepository shiftRepository;

    @Override
    public boolean isValid(CreateShiftChangeRequest request, ConstraintValidatorContext context) {
        boolean valid = true;

        // 1) Verificar existencia de offeredShift
        Optional<Shift> optOffered = shiftRepository.findById(request.getOfferedShiftId());
        if (optOffered.isEmpty()) {
            ValidationUtils.addValidationError(
                    context,
                    "offeredShiftId",
                    "Offered shift with ID " + request.getOfferedShiftId() + " does not exist"
            );
            valid = false;
        }

        // 2) Verificar existencia de desiredShift
        Optional<Shift> optDesired = shiftRepository.findById(request.getDesiredShiftId());
        if (optDesired.isEmpty()) {
            ValidationUtils.addValidationError(
                    context,
                    "desiredShiftId",
                    "Desired shift with ID " + request.getDesiredShiftId() + " does not exist"
            );
            valid = false;
        }

        // Si alguno no existe, abortar validaciones posteriores
        if (!valid) {
            return false;
        }

        Shift offeredShift = optOffered.get();
        Shift desiredShift = optDesired.get();

        // 3) No pueden ser el mismo turno
        if (request.getOfferedShiftId().equals(request.getDesiredShiftId())) {
            ValidationUtils.addValidationError(
                    context,
                    "desiredShiftId",
                    "Shifts to change cannot be the same"
            );
            valid = false;
        }

        // 4) No puede ser la misma enfermera
        if (request.getRequestingNurseId().equals(request.getReceivingNurseId())) {
            ValidationUtils.addValidationError(
                    context,
                    "requestingNurseId",
                    "Nurse requesting cannot be the same as receiving nurse"
            );
            valid = false;
        }

        // 5) Ambos turnos deben pertenecer al mismo departamento
        if (!offeredShift.getDepartment().getId().equals(desiredShift.getDepartment().getId())) {
            ValidationUtils.addValidationError(
                    context,
                    "offeredShiftId",
                    "Offered shift and desired shift must belong to the same department"
            );
            valid = false;
        }

        return valid;
    }
}