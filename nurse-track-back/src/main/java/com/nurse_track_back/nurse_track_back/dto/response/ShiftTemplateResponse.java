package com.nurse_track_back.nurse_track_back.dto.response;

import com.nurse_track_back.nurse_track_back.domain.enums.ShiftType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private LocalTime startTime;
    private LocalTime endTime;
    private ShiftType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
