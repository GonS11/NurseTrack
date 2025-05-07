package com.nursetrack.web.dto.request.shiftChange;

import com.nursetrack.domain.enums.Status;
import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.validations.annotations.ValidShiftChangeRequest;
import com.nursetrack.validations.annotations.ValidShiftId;
import com.nursetrack.validations.annotations.ValidUserId;
import com.nursetrack.validations.annotations.ValidUserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ValidShiftChangeRequest
public class CreateShiftChangeRequest 
{
    @NotNull(message = "Requesting nurse ID is required")
    @Positive(message = "Requesting nurse ID must be a positive number")
    @ValidUserRole(allowedRoles = {UserRole.NURSE})
    @ValidUserId
    private Long requestingNurseId;

    @NotNull(message = "Offered shift ID is required")
    @Positive(message = "Offered shift ID must be a positive number")
    @ValidShiftId
    private Long offeredShiftId;

    @NotNull(message = "Receiving nurse ID is required")
    @Positive(message = "Receiving nurse ID must be a positive number")
    @ValidUserRole(allowedRoles = {UserRole.NURSE})
    @ValidUserId
    private Long receivingNurseId;

    @NotNull(message = "Desired shift ID is required")
    @Positive(message = "Desired shift ID must be a positive number")
    @ValidShiftId
    private Long desiredShiftId;

    private Status status = Status.PENDING;

    @NotBlank(message = "Reason is required")
    @Size(max = 1000, message = "Reason cannot exceed 1000 characters")
    private String reason;
}
