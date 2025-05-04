package com.nursetrack.validations.validators;

import com.nursetrack.domain.model.Shift;
import com.nursetrack.repository.ShiftRepository;
import com.nursetrack.utils.ValidationUtils;
import com.nursetrack.validations.annotations.ValidShiftChangeRequest;
import com.nursetrack.web.dto.request.shiftChange.CreateShiftChangeRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiftChangeRequestValidator implements ConstraintValidator<ValidShiftChangeRequest, CreateShiftChangeRequest> 
{
    @Autowired
    private ShiftRepository shiftRepository;

    @Override
    public boolean isValid(CreateShiftChangeRequest request, ConstraintValidatorContext context) 
    {
        boolean valid = true;

        Shift offeredShift = shiftRepository.findById(request.getOfferedShiftId()).orElseThrow();
        Shift desiredShift = shiftRepository.findById(request.getDesiredShiftId()).orElseThrow();


        //Cambio del mismo turno
        if (request.getDesiredShiftId().equals(request.getOfferedShiftId()))
        {
            ValidationUtils.addValidationError(context, "desiredShiftId",
                                               "Shifts to change cannot be the same");
            valid = false;
        }

        //Cambios de la misma enfermera
        if(request.getRequestingNurseId().equals(request.getReceivingNurseId()))
        {
            ValidationUtils.addValidationError(context, "requestingNurseId",
                                               "Nurse requesting cannot be the same as receiving nurse");
            valid =false;
        }

        //Deben pertenecer ambos turnos al mismo departamento
        if(!offeredShift.getDepartment().equals(desiredShift.getDepartment()))
        {
            ValidationUtils.addValidationError(context, "offeredShiftId",
                                               "Shift offered and desired to change must belong to the same department");
            valid =false;
        }



        return valid;
    }
}