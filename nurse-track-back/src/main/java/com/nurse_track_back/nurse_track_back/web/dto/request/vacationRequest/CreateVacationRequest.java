package com.nurse_track_back.nurse_track_back.web.dto.request.vacationRequest;

import com.nurse_track_back.nurse_track_back.domain.enums.RequestStatus;
import com.nurse_track_back.nurse_track_back.domain.enums.Role;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidUserId;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidUserRole;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidVacationRequest;
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
    @ValidUserRole(allowedRoles = {Role.ROLE_NURSE}, message = "User must have the role of Nurse")
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

    private RequestStatus status = RequestStatus.PENDING;

    @Positive(message = "Reviewed by ID must be a positive number")
    @ValidUserRole(allowedRoles = {Role.ROLE_SUPERVISOR}, message = "User must have the role of Supervisor")
    @ValidUserId(message = "Invalid supervisor ID")
    private Long reviewedById;
}