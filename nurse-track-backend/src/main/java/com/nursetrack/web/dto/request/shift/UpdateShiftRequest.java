package com.nursetrack.web.dto.request.shift;

import com.nursetrack.domain.enums.Status;
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
@ValidShiftUpdate
public class UpdateShiftRequest
{
    @Positive
    @ValidUserRole(allowedRoles = {UserRole.NURSE})
    @ValidUserId
    private Long nurseId;

    @Positive
    @ValidDepartmentId
    private Long departmentId;

    @Positive
    @ValidShiftTemplateId
    private Long shiftTemplateId;

    private Status status;

    @FutureOrPresent
    private LocalDate shiftDate;

    @Size(max = 500)
    private String notes;
}