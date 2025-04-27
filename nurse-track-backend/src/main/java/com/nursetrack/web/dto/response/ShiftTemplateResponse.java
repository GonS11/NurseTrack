package com.nursetrack.web.dto.response;

import com.nursetrack.domain.enums.ShiftType;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftTemplateResponse
{
    private Long id;
    private String name;
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;
    private ShiftType shiftType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}