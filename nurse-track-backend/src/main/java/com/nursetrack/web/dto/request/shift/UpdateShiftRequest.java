package com.nursetrack.web.dto.request.shift;

import com.nursetrack.domain.enums.ShiftStatus;
import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.validations.annotations.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ValidShiftRequest
public class UpdateShiftRequest 
{
    @Positive(message = "Nurse ID must be a positive number")
    @ValidUserRole(allowedRoles = {UserRole.NURSE})
    @ValidUserId
    private Long nurseId;

    @Positive(message = "Department ID must be a positive number")
    @ValidDepartmentId
    private Long departmentId;

    @Positive(message = "Shift template ID must be a positive number")
    @ValidShiftTemplateId
    private Long shiftTemplateId;

    private ShiftStatus status;

    @FutureOrPresent(message = "Shift date must be today or in the future")
    private LocalDate shiftDate;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}
