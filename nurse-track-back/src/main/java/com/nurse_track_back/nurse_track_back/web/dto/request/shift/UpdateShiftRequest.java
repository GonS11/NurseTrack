package com.nurse_track_back.nurse_track_back.web.dto.request.shift;

import com.nurse_track_back.nurse_track_back.domain.enums.Role;
import com.nurse_track_back.nurse_track_back.domain.enums.ShiftStatus;
import com.nurse_track_back.nurse_track_back.validations.annotations.*;
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
public class UpdateShiftRequest
{
    @NotNull
    @Positive(message = "Nurse ID must be a positive number")
    @ValidUserRole(allowedRoles = {Role.NURSE})
    @ValidUserId
    private Long nurseId;

    @NotNull
    @Positive(message = "Department ID must be a positive number")
    @ValidDepartmentId
    private Long departmentId;

    @Positive(message = "Shift template ID must be a positive number")
    @ValidShiftTemplateId
    private Long shiftTemplateId;

    @FutureOrPresent(message = "Shift date must be today or in the future")
    private LocalDate shiftDate;

    private ShiftStatus status;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}
