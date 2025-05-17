package com.nurse_track_back.nurse_track_back.validations.validators;

import com.nurse_track_back.nurse_track_back.domain.models.SupervisorDepartment;
import com.nurse_track_back.nurse_track_back.web.dto.request.supervisorDepartment.AssignSupervisorRequest;
import com.nurse_track_back.nurse_track_back.repositories.SupervisorDepartmentRepository;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidSupervisorAssignment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class SupervisorAssignmentValidator implements ConstraintValidator<ValidSupervisorAssignment, AssignSupervisorRequest>
{
    @Autowired
    private SupervisorDepartmentRepository supervisorDepartmentRepository;

    @Override
    public boolean isValid(AssignSupervisorRequest request, ConstraintValidatorContext context)
    {
        boolean isValid = true;

        // Verificar si el departamento ya tiene supervisor
        Optional<SupervisorDepartment> existingDeptAssignment = supervisorDepartmentRepository.findByDepartmentId(request.getDepartmentId());

        if (existingDeptAssignment.isPresent() &&
                !existingDeptAssignment.get().getSupervisor().getId().equals(request.getSupervisorId()))
        {
            context.buildConstraintViolationWithTemplate("Department already has one supervisor assigned")
                    .addPropertyNode("departmentId")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}