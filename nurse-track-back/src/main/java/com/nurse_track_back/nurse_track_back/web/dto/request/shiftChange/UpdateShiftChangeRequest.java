package com.nurse_track_back.nurse_track_back.web.dto.request.shiftChange;

import com.nurse_track_back.nurse_track_back.domain.enums.RequestStatus;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidEnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateShiftChangeRequest
{
    @ValidEnumValue(
            enumClass = RequestStatus.class,
            allowedValues = {"APPROVED", "REJECTED", "CANCELLED"},
            message = "Status must be one of: APPROVED, REJECTED, or CANCELLED"
    )
    private RequestStatus status;

    @NotBlank
    @Size(max = 1000, message = "Reviewed notes cannot exceed 1000 characters")
    private String reviewedNotes = "";
}