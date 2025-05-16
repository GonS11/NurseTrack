package com.nurse_track_back.nurse_track_back.validation.validators;

import com.nurse_track_back.nurse_track_back.dto.request.supervisorDepartment.AssignSupervisorRequest;
import com.nurse_track_back.nurse_track_back.repository.SupervisorDepartmentRepository;
import com.nurse_track_back.nurse_track_back.utils.ValidationUtils;
import com.nurse_track_back.nurse_track_back.validation.annotations.ValidSupervisorAssignment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class SupervisorAssignmentValidator implements ConstraintValidator<ValidSupervisorAssignment, AssignSupervisorRequest>
{
    @Autowired
    private SupervisorDepartmentRepository supervisorDepartmentRepository;

    @Override
    public boolean isValid(AssignSupervisorRequest request, ConstraintValidatorContext context)
    {
        boolean valid = true;

        // Verificar si supervisor ya asignado a otro departamento
        supervisorDepartmentRepository.findBySupervisorId(request.getSupervisorId())
                .filter(assignment -> !assignment.getDepartment().getId().equals(request.getDepartmentId()))
                .ifPresent(assignment -> {
                    ValidationUtils.addValidationError(context, "supervisorId", "Supervisor already assign to "
                            + assignment.getDepartment().getName() + " department");
                });

        // Verificar si departamento tiene un supervisor
        supervisorDepartmentRepository.findByDepartmentId(request.getDepartmentId())
                .filter(assignment -> !assignment.getSupervisor().getId().equals(request.getSupervisorId()))
                .ifPresent(assignment -> {
                    ValidationUtils.addValidationError(context, "departmentId","Department already has assigned supervisor with name: "
                            + assignment.getSupervisor().getFirstname() + " "
                            + assignment.getSupervisor().getLastname());
                });

        return valid;
    }
}
