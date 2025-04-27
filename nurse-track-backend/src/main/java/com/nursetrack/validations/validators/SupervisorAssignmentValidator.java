package com.nursetrack.validations.validators;

import com.nursetrack.repository.SupervisorDepartmentRepository;
import com.nursetrack.validations.annotations.ValidSupervisorAssignment;
import com.nursetrack.web.dto.request.supervisorDepartment.AssignSupervisorRequest;
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
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Supervisor already assign to " + assignment.getDepartment().getName() + " department")
                            .addPropertyNode("supervisorId")
                            .addConstraintViolation();
                });

        // Verificar si departamento tiene un supervisor
        supervisorDepartmentRepository.findByDepartmentId(request.getDepartmentId())
                .filter(assignment -> !assignment.getSupervisor().getId().equals(request.getSupervisorId()))
                .ifPresent(assignment -> {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Department already has assigned supervisor with name: "
                                                                         + assignment.getSupervisor().getFirstName() + " "
                                                                         + assignment.getSupervisor().getLastName())
                            .addPropertyNode("departmentId")
                            .addConstraintViolation();
                });

        return valid;
    }
}
