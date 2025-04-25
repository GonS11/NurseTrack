package com.nursetrack.domain.enums;

import lombok.Getter;

@Getter
public enum NotificationType
{
    SHIFT_CHANGE("Change of shift", "swap_horiz"),
    VACATION_REQUEST("Vacation request", "beach_access"),
    GENERAL("General notification", "info"),
    SYSTEM("System notification", "settings"),
    EMERGENCY("Emergency notification", "warning");

    private final String displayName;
    private final String icon; // Para uso en frontend

    NotificationType(String displayName, String icon)
    {
        this.displayName = displayName;
        this.icon = icon;
    }
}
