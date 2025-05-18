package com.nurse_track_back.nurse_track_back.validations.validators;

import com.nurse_track_back.nurse_track_back.repositories.ShiftTemplateRepository;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidShiftTemplateId;
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
