package com.nurse_track_back.nurse_track_back.web.dto.request.vacationRequest;

import com.nurse_track_back.nurse_track_back.domain.enums.RequestStatus;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidVacationRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ValidVacationRequest
public class UpdateVacationRequest {
    private RequestStatus status;

    @NotBlank
    @Size(max = 2000, message = "Reviewed notes cannot exceed 2000 characters")
    private String reviewedNotes;
}
