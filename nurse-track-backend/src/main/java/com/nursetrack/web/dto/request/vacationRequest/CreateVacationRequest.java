package com.nursetrack.web.dto.request.vacationRequest;

import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.validations.annotations.ValidUserId;
import com.nursetrack.validations.annotations.ValidUserRole;
import com.nursetrack.validations.annotations.ValidVacationRequest;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ValidVacationRequest
public class CreateVacationRequest
{
    @NotNull
    @Positive
    @ValidUserRole(allowedRoles = {UserRole.NURSE})
    @ValidUserId
    private Long requestingNurseId;

    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

    @NotNull
    @FutureOrPresent
    private LocalDate endDate;

    @NotBlank
    @Size(max = 2000)
    private String reason;

    @NotNull
    @Positive
    @ValidUserRole(allowedRoles = {UserRole.SUPERVISOR})
    @ValidUserId
    private Long reviewedById;
}