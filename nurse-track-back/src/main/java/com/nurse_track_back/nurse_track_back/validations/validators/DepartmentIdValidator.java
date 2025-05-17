package com.nurse_track_back.nurse_track_back.validations.validators;

import com.nurse_track_back.nurse_track_back.repositories.DepartmentRepository;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidDepartmentId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class DepartmentIdValidator implements ConstraintValidator<ValidDepartmentId, Long>
{
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public boolean isValid(Long departmentId, ConstraintValidatorContext context)
    {
        if(departmentId == null) return true;

        return departmentRepository.existsById(departmentId);
    }
}

