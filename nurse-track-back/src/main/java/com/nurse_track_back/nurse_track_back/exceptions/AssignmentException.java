package com.nurse_track_back.nurse_track_back.exceptions;

public class AssignmentException extends BusinessException
{
    // Constructor para casos generales
    public AssignmentException(String message) {
        super(message);
    }

    // Fx factory para simplificar creación
    public static AssignmentException create(String resourceName, Object resourceId, Object assignedToId)
    {
        String message = String.format("%s with identifier %s cannot be a assigned to department with identifier %s", resourceName, resourceId, assignedToId);
        return new AssignmentException(message);
    }
}