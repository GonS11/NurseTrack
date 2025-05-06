package com.nursetrack.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nursetrack.domain.enums.Status;
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
    private UserSimpleResponse requester;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String reviewedNotes;
    private Status status;
    private UserSimpleResponse reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;

    // Campo calculado para duración
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // Solo lectura
    public long getDurationInDays()
    {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
