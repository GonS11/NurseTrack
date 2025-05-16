package com.nurse_track_back.nurse_track_back.validation.validators;

import com.nurse_track_back.nurse_track_back.repository.DepartmentRepository;
import com.nurse_track_back.nurse_track_back.validation.annotations.ValidDepartmentId;
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

