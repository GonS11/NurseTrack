package com.nursetrack.exception;

public class LicenseNumberAlreadyExistsException extends RuntimeException
{
    public LicenseNumberAlreadyExistsException(String licenseNumber)
    {
        super("License number already exists: " + licenseNumber);
    }
}
