package com.nursetrack.validations.validators;

import com.nursetrack.repository.DepartmentRepository;
import com.nursetrack.validations.annotations.ValidDepartmentName;
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
        if(departmentName == null) return false;

       return !departmentRepository.existsByName(departmentName);
    }
}
