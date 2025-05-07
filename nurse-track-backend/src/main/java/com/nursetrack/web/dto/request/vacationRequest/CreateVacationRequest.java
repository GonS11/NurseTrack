package com.nursetrack.web.dto.request.vacationRequest;

import com.nursetrack.domain.enums.Status;
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
    @NotNull(message = "Requesting nurse ID is required")
    @Positive(message = "Requesting nurse ID must be a positive number")
    @ValidUserRole(allowedRoles = {UserRole.NURSE}, message = "User must have the role of Nurse")
    @ValidUserId(message = "Invalid nurse ID")
    private Long requestingNurseId;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in the future or present")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be in the future or present")
    private LocalDate endDate;

    @NotBlank(message = "Reason is required")
    @Size(max = 2000, message = "Reason cannot exceed 2000 characters")
    private String reason;

    private Status status = Status.PENDING;

    @NotNull(message = "Reviewed by ID is required")
    @Positive(message = "Reviewed by ID must be a positive number")
    @ValidUserRole(allowedRoles = {UserRole.SUPERVISOR}, message = "User must have the role of Supervisor")
    @ValidUserId(message = "Invalid supervisor ID")
    private Long reviewedById;
}
