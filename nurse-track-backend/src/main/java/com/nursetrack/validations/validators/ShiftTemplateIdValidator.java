package com.nursetrack.validations.validators;

import com.nursetrack.repository.ShiftTemplateRepository;
import com.nursetrack.validations.annotations.ValidShiftTemplateId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiftTemplateIdValidator implements ConstraintValidator<ValidShiftTemplateId,Long>
{
    @Autowired
    private ShiftTemplateRepository shiftTemplateRepository;

    @Override
    public boolean isValid(Long shiftTemplateId, ConstraintValidatorContext context)
    {
        if(shiftTemplateId == null) return true;

        return shiftTemplateRepository.existsById(shiftTemplateId);
    }
}
