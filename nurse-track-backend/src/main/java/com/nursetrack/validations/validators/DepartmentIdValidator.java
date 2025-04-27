package com.nursetrack.validations.validators;

import com.nursetrack.repository.DepartmentRepository;
import com.nursetrack.validations.annotations.ValidDepartmentId;
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
