package com.nursetrack.web.dto.request.vacationRequest;

import com.nursetrack.domain.enums.Status;
import com.nursetrack.validations.annotations.ValidVacationRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ValidVacationRequest
public class UpdateVacationRequest 
{
    @NotNull(message = "Status is required")
    private Status status;

    @Size(max = 2000, message = "Reviewed notes cannot exceed 2000 characters")
    private String reviewedNotes; // Mandatory only for REJECTED status
}
