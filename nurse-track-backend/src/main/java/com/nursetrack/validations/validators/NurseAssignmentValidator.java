package com.nursetrack.validations.validators;

import com.nursetrack.repository.NurseDepartmentRepository;
import com.nursetrack.utils.ValidationUtils;
import com.nursetrack.validations.annotations.ValidNurseAssignment;
import com.nursetrack.web.dto.request.nurseDepartment.AssignNurseRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class NurseAssignmentValidator implements ConstraintValidator<ValidNurseAssignment, AssignNurseRequest>
{
    @Autowired
    private NurseDepartmentRepository nurseDepartmentRepository;

    @Override
    public boolean isValid(AssignNurseRequest request, ConstraintValidatorContext context)
    {
        if (nurseDepartmentRepository.existsByNurseIdAndDepartmentId(request.getNurseId(), request.getDepartmentId()))
        {
            ValidationUtils.addValidationError(context, "nurseId", "Nurse with id " + request.getNurseId()
                                                       + " is already assigned to the department with id "
                                                       + request.getDepartmentId());

            return false;
        }
        return true;
    }
}
