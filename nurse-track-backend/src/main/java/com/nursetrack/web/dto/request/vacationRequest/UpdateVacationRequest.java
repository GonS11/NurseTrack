package com.nursetrack.web.dto.request.vacationRequest;

import com.nursetrack.domain.enums.Status;
import com.nursetrack.validations.annotations.ValidVacationRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ValidVacationRequest
public class UpdateVacationRequest
{
    @NotNull
    private Status status;

    @Positive
    private Integer reviewerUserId;  // Obligatorio solo si status != PENDING

    @Size(max = 2000)
    private String reviewNotes;
}