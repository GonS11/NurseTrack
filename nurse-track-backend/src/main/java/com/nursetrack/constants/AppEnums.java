package com.nursetrack.constants;

import lombok.Getter;

public class AppEnums
{
    @Getter
    public enum UserRole
    {
        ADMIN("Administrator", "Has full access to the system"),
        SUPERVISOR("Supervisor","Manages staff and schedule"),
        NURSE("Nurse","Basic access to the system");

        private final String displayName;
        private final String description;

        UserRole(String displayName, String description)
        {
            this.displayName = displayName;
            this.description = description;
        }
    }

    @Getter
    public enum ShiftTypes
    {
        MORNING("Morning Shift","08:00 - 15:00"),
        AFTERNOON("Afternoon Shift","15:00 - 22:00"),
        NIGHT("Night Shift","22:00 - 08:00"),
        HALF_MORNING("12 hours Morning","08:00 - 20:00"),
        HALF_NIGHT("12 hours Night","20:00 - 08:00");

        private final String displayName;
        private final String timeRange;

        ShiftTypes(String displayName, String timeRange)
        {
            this.displayName = displayName;
            this.timeRange = timeRange;
        }
    }

    @Getter
    public enum Status {
        SCHEDULED("Scheduled"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled"),
        PENDING("Pending"),
        APPROVED("Approved"),
        REJECTED("Rejected");

        private final String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }
    }

    @Getter
    public enum NotificationType {
        SHIFT_CHANGE("Change of shift"),
        VACATION_REQUEST("Vacation request"),
        GENERAL("General notification"),
        SYSTEM("System notification"),
        EMERGENCY("Emergency notification");

        private final String displayName;

        NotificationType(String displayName) {
            this.displayName = displayName;
        }
    }
}
