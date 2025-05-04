package com.nursetrack.domain.enums;

import lombok.Getter;

@Getter
public enum ShiftStatus
{
    SCHEDULED("Scheduled", "primary"),
    COMPLETED("Completed", "success"),
    CANCELLED("Cancelled", "danger"),
    SWAPPED("Swapped", "swapped");

    private final String displayName;
    private final String badgeStyle; // Para UI

    ShiftStatus(String displayName, String badgeStyle)
    {
        this.displayName = displayName;
        this.badgeStyle = badgeStyle;
    }
}
