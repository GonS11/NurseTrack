package com.nursetrack.validations.validators;

import com.nursetrack.domain.enums.Status;
import com.nursetrack.repository.DepartmentRepository;
import com.nursetrack.repository.ShiftRepository;
import com.nursetrack.repository.ShiftTemplateRepository;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.utils.ValidationUtils;
import com.nursetrack.validations.annotations.ValidShiftUpdate;
import com.nursetrack.web.dto.request.shift.UpdateShiftRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class ShiftUpdateValidator implements ConstraintValidator<ValidShiftUpdate, UpdateShiftRequest>
{

    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShiftTemplateRepository shiftTemplateRepository;

    @Override
    public boolean isValid(UpdateShiftRequest request, ConstraintValidatorContext context)
    {
        boolean isValid = true;

        // Validar nurseId y status
        if (request.getNurseId() != null
                && request.getStatus() != null
                && !List.of(Status.SCHEDULED, Status.APPROVED).contains(request.getStatus()))
        {
            ValidationUtils.addValidationError(context, "status",
                                               "Shifts with assigned nurses must be SCHEDULED or APPROVED");
            isValid = false;
        }

        // Validar departamento existe
        if (!departmentRepository.existsById(request.getDepartmentId()))
        {
            ValidationUtils.addValidationError(context, "departmentId", "Department does not exist");
            isValid = false;
        }

        // Validar fecha
        if (request.getShiftDate() != null
                && request.getShiftDate().isAfter(LocalDate.now().plusYears(1)))
        {
            ValidationUtils.addValidationError(context, "shiftDate",
                                               "Shift date cannot be more than 1 year in the future");
            isValid = false;
        }

        // Validar planilla existe
        if (request.getShiftTemplateId() != null
                && !shiftTemplateRepository.existsById(request.getShiftTemplateId()))
        {
            ValidationUtils.addValidationError(context, "shiftTemplateId","Shift template does not exist");
            isValid = false;
        }

        return isValid;
    }
}