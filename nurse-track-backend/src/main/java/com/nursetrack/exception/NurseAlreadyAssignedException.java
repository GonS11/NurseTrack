package com.nursetrack.exception;

public class NurseAlreadyAssignedException extends RuntimeException
{
    public NurseAlreadyAssignedException(Long nurseId, Long departmentId)
    {
        super("User with id " + nurseId + " and role of nurse already belongs to department with id " + departmentId);
    }
}
