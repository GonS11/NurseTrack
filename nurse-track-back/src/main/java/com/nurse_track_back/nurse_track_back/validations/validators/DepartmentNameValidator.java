package com.nurse_track_back.nurse_track_back.validations.validators;

import com.nurse_track_back.nurse_track_back.repositories.DepartmentRepository;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidDepartmentName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class DepartmentNameValidator implements ConstraintValidator<ValidDepartmentName, String>
{
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public boolean isValid(String departmentName, ConstraintValidatorContext context)
    {
        if(departmentName == null) return true;

        return !departmentRepository.existsByName(departmentName);
    }
}