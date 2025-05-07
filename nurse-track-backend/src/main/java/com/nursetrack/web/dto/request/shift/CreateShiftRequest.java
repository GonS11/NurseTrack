package com.nursetrack.web.dto.request.shift;

import com.nursetrack.domain.enums.ShiftStatus;
import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.validations.annotations.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
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
public class CreateShiftRequest 
{
    @NotNull(message = "Nurse ID is required")
    @Positive(message = "Nurse ID must be a positive number")
    @ValidUserRole(allowedRoles = {UserRole.NURSE})
    @ValidUserId
    private Long nurseId;

    @NotNull(message = "Department ID is required")
    @Positive(message = "Department ID must be a positive number")
    @ValidDepartmentId
    private Long departmentId;

    @NotNull(message = "Shift template ID is required")
    @Positive(message = "Shift template ID must be a positive number")
    @ValidShiftTemplateId
    private Long shiftTemplateId;

    @NotNull(message = "Shift date is required")
    @FutureOrPresent(message = "Shift date must be today or in the future")
    private LocalDate shiftDate;

    private ShiftStatus status = ShiftStatus.SCHEDULED;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    @NotNull(message = "Created by ID is required")
    @Positive(message = "Created by ID must be a positive number")
    @ValidUserRole(allowedRoles = {UserRole.SUPERVISOR})
    @ValidUserId
    private Long createdById;
}

