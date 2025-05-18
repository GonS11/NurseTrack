package com.nurse_track_back.nurse_track_back.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nurse_track_back.nurse_track_back.domain.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacationRequestResponse
{
    private Long id;
    private UserSimpleResponse requestingNurse;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String reviewedNotes;
    private RequestStatus status;
    private UserSimpleResponse reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public long getDurationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
