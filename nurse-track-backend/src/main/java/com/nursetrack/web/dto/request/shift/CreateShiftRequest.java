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
    @NotNull
    @Positive
    @ValidUserRole(allowedRoles = {UserRole.NURSE})
    @ValidUserId
    private Long nurseId; //Nullable para turno sin enfermera

    @NotNull
    @Positive
    @ValidDepartmentId
    private Long departmentId;

    @NotNull
    @Positive
    @ValidShiftTemplateId
    private Long shiftTemplateId;

    @NotNull
    @FutureOrPresent
    private LocalDate shiftDate;

    private ShiftStatus status = ShiftStatus.SCHEDULED;

    @Size(max = 500)
    private String notes;

    @NotNull
    @Positive
    @ValidUserRole(allowedRoles = {UserRole.SUPERVISOR})
    @ValidUserId
    private Long createdById;
}
