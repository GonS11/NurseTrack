package com.nursetrack.exception;

public class NurseAssignmentNotFoundException extends RuntimeException
{
    public NurseAssignmentNotFoundException(Long nurseId, Long departmentId)
    {
      super("User with id " + nurseId + " and role of nurse does not belong to department with id " + departmentId);
    }
}
