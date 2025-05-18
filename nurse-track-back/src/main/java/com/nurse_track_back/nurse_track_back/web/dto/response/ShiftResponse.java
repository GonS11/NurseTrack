package com.nurse_track_back.nurse_track_back.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nurse_track_back.nurse_track_back.domain.enums.ShiftStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftResponse
{
    private Long id;
    private UserSimpleResponse nurse;
    private DepartmentResponse department;
    private ShiftTemplateResponse shiftTemplate;
    private LocalDate shiftDate;
    private ShiftStatus status;
    private String notes;
    private UserSimpleResponse createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDateTime getShiftStart() {
        return LocalDateTime.of(shiftDate, shiftTemplate.getStartTime());
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDateTime getShiftEnd() {
        return shiftTemplate.getEndTime().isBefore(shiftTemplate.getStartTime()) ?
                LocalDateTime.of(shiftDate.plusDays(1), shiftTemplate.getEndTime()) :
                LocalDateTime.of(shiftDate, shiftTemplate.getEndTime());
    }
}
