package com.nursetrack.web.dto.request.shiftChange;

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
    @NotNull
    @Positive
    @ValidUserRole(allowedRoles = {UserRole.NURSE})
    @ValidUserId
    private Long requestingNurseId;

    @NotNull
    @Positive
    @ValidShiftId
    private Long offeredShiftId;

    @NotNull
    @Positive
    @ValidUserRole(allowedRoles = {UserRole.NURSE})
    @ValidUserId
    private Long receivingNurseId;

    @NotNull
    @Positive
    @ValidShiftId
    private Long desiredShiftId;

    @NotBlank
    @Size(max = 1000)
    private String reason;
}