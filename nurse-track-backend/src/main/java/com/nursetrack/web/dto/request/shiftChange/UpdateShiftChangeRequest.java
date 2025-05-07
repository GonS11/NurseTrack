package com.nursetrack.web.dto.request.shiftChange;

import com.nursetrack.domain.enums.Status;
import com.nursetrack.validations.annotations.ValidEnumValue;
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
    @NotNull(message = "Status is required")
    @ValidEnumValue(
            enumClass = Status.class,
            allowedValues = {"APPROVED", "REJECTED", "CANCELLED"},
            message = "Status must be one of: APPROVED, REJECTED, or CANCELLED"
    )
    private Status status;

    @NotNull(message = "Reviewed notes are required")
    @Size(max = 1000, message = "Reviewed notes cannot exceed 1000 characters")
    private String reviewedNotes;
}
