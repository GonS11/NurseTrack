package com.nursetrack.domain.enums;

import lombok.Getter;

@Getter
public enum Status
{
    SCHEDULED("Scheduled", "primary"),
    COMPLETED("Completed", "success"),
    CANCELLED("Cancelled", "danger"),
    PENDING("Pending", "warning"),
    APPROVED("Approved", "info"),
    REJECTED("Rejected", "secondary");

    private final String displayName;
    private final String badgeStyle; // Para UI

    Status(String displayName, String badgeStyle)
    {
        this.displayName = displayName;
        this.badgeStyle = badgeStyle;
    }
}
