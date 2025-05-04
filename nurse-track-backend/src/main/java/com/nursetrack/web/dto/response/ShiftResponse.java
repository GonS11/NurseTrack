package com.nursetrack.web.dto.response;

import com.nursetrack.domain.enums.ShiftStatus;
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

    public LocalDateTime getShiftStart()
    {
        return LocalDateTime.of(shiftDate, shiftTemplate.getShiftStartTime());
    }

    public LocalDateTime getShiftEnd()
    {
        return shiftTemplate.getShiftEndTime().isBefore(shiftTemplate.getShiftStartTime()) ?
                LocalDateTime.of(shiftDate.plusDays(1), shiftTemplate.getShiftEndTime()) :
                LocalDateTime.of(shiftDate, shiftTemplate.getShiftEndTime());
    }
}