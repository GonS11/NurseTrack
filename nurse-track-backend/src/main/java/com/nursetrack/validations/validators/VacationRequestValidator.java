package com.nursetrack.validations.validators;

import com.nursetrack.domain.enums.Status;
import com.nursetrack.utils.ValidationUtils;
import com.nursetrack.validations.annotations.ValidVacationRequest;
import com.nursetrack.web.dto.request.vacationRequest.CreateVacationRequest;
import com.nursetrack.web.dto.request.vacationRequest.UpdateVacationRequest;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class VacationRequestValidator implements ConstraintValidator<ValidVacationRequest, Object>
{
    @Override
    public boolean isValid(Object request, ConstraintValidatorContext context)
    {
        if (request instanceof CreateVacationRequest createReq)
        {
            return validateCreation(createReq, context);
        }
        else if (request instanceof UpdateVacationRequest updateReq)
        {
            return validateUpdate(updateReq, context);
        }

        return true;
    }

    private boolean validateCreation(CreateVacationRequest request,
                                     ConstraintValidatorContext context)
    {
        boolean valid = true;

        // Validad fin >= inicio
        if (request.getEndDate().isBefore(request.getStartDate()))
        {
            ValidationUtils.addValidationError(context, "endDate",
                                               "End date must be after start date");
            valid = false;
        }

        // Maximo numero de peticiones (30 dias seguidos)
        long daysBetween = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());

        if(daysBetween > 30)
        {
            ValidationUtils.addValidationError(context, "endDate",
                                               "The maximum vacation duration is 30 days");
            valid = false;
        }

        // Antelacion minima (48 horas antes)
        if(request.getStartDate().isBefore(LocalDate.now().plusDays(2)))
        {
            ValidationUtils.addValidationError(context, "startDate",
                                               "Vacation must be requested at least 48 hours in advance");
            valid = false;
        }

        return valid;
    }

    private boolean validateUpdate(UpdateVacationRequest request,
                                   ConstraintValidatorContext context)
    {
        if (request.getStatus() == Status.REJECTED &&
                StringUtils.isBlank(request.getReviewedNotes()))
        {
            ValidationUtils.addValidationError(context, "reviewNotes",
                                               "Review notes are required when rejecting");
            return false;
        }

        return true;
    }
}