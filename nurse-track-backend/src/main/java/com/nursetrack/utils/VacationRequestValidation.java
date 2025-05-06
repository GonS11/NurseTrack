package com.nursetrack.utils;

import com.nursetrack.domain.enums.Status;
import com.nursetrack.domain.model.VacationRequest;
import com.nursetrack.exception.InvalidRequestException;
import com.nursetrack.repository.SupervisorDepartmentRepository;
import com.nursetrack.repository.VacationRequestRepository;
import com.nursetrack.web.dto.request.vacationRequest.CreateVacationRequest;
import com.nursetrack.web.dto.request.vacationRequest.UpdateVacationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VacationRequestValidation
{
    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    @Autowired
    private SupervisorDepartmentRepository supervisorDepartmentRepository;

    public void validateVacationRequestCreation(CreateVacationRequest request)
    {
        validateSupervisor(request.getRequestingNurseId(), request.getReviewedById());
        validateDateOverlap(request.getRequestingNurseId(), request.getStartDate(), request.getEndDate());
    }
    public void validateVacationRequestUpdate(VacationRequest existingRequest, UpdateVacationRequest newRequest)
    {
        validatePendingStatus(existingRequest);
        validateReviewerForApproval(existingRequest, newRequest.getStatus());
    }

    private void validateSupervisor(Long nurseId, Long supervisorId)
    {
        if(!supervisorDepartmentRepository.existsByNurseIdAndSupervisorId(nurseId, supervisorId))
        {
            throw new InvalidRequestException("Supervisor does not belongs to nurse's department");
        }
    }

    private void validateDateOverlap(Long nurseId, LocalDate startDate, LocalDate endDate)
    {
        if (vacationRequestRepository.existsByRequesterAndStatusAndDateRangeOverlap(nurseId, Status.APPROVED,
                                                                                    startDate, endDate))
        {
            throw new InvalidRequestException("There is already a vacation request approved for this date range");
        }
    }

    private void validatePendingStatus(VacationRequest request)
    {
        if(request.getStatus() != Status.PENDING)
        {
            throw new InvalidRequestException("Only the PENDING request could be modified");
        }
    }

    private void validateReviewerForApproval(VacationRequest request, Status newStatus)
    {
        if((newStatus == Status.APPROVED || newStatus == Status.REJECTED)
            && request.getReviewedBy() == null)
        {
            throw new InvalidRequestException("Reviewer it is required to approved/reject the request");
        }
    }


}
