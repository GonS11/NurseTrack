package com.nursetrack.validations.validators;

import com.nursetrack.repository.ShiftRepository;
import com.nursetrack.validations.annotations.ValidShiftId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiftIdValidator implements ConstraintValidator<ValidShiftId,Long>
{
    @Autowired
    private ShiftRepository shiftRepository;

    @Override
    public boolean isValid(Long shiftId, ConstraintValidatorContext context)
    {
        if(shiftId == null) return true;

        return shiftRepository.existsById(shiftId);
    }
}
