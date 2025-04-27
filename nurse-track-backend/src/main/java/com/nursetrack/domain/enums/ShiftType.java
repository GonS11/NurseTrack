package com.nursetrack.domain.enums;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public enum ShiftType
{
    MORNING("Morning Shift", LocalTime.of(8, 0), LocalTime.of(15, 0), true, false),
    AFTERNOON("Afternoon Shift", LocalTime.of(15, 0), LocalTime.of(22, 0), true, false),
    NIGHT("Night Shift", LocalTime.of(22, 0), LocalTime.of(8, 0), true, true),
    HALF_MORNING("12 hours Morning", LocalTime.of(8, 0), LocalTime.of(20, 0), true, false),
    HALF_NIGHT("12 hours Night", LocalTime.of(20, 0), LocalTime.of(8, 0), true, true);

    private final String displayName;
    private final LocalTime defaultStartTime;
    private final LocalTime defaultEndTime;

    @Getter
    private final Boolean fixedTime;
    @Getter
    private final Boolean overnight;

    ShiftType(String displayName, LocalTime defaultStartTime,
              LocalTime defaultEndTime, Boolean fixedTime, Boolean overnight)
    {
        this.displayName = displayName;
        this.defaultStartTime = defaultStartTime;
        this.defaultEndTime = defaultEndTime;
        this.fixedTime = fixedTime;
        this.overnight = overnight;
    }

}