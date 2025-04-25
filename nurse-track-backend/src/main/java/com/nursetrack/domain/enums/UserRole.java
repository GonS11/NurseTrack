package com.nursetrack.domain.enums;

import lombok.Getter;

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
