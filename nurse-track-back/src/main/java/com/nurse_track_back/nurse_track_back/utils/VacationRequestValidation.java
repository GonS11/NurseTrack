package com.nurse_track_back.nurse_track_back.utils;

import com.nurse_track_back.nurse_track_back.domain.enums.RequestStatus;
import com.nurse_track_back.nurse_track_back.domain.models.VacationRequest;
import com.nurse_track_back.nurse_track_back.exceptions.InvalidRequestException;
import com.nurse_track_back.nurse_track_back.repositories.SupervisorDepartmentRepository;
import com.nurse_track_back.nurse_track_back.repositories.VacationRequestRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.vacationRequest.CreateVacationRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.vacationRequest.UpdateVacationRequest;
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
    public void validateVacationRequestUpdate(VacationRequest existingRequest, UpdateVacationRequest newRequest, Long currentUserId)
    {
        validatePendingStatus(existingRequest);
        validateReviewerForApproval(existingRequest, newRequest.getStatus(),currentUserId);
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
        if (vacationRequestRepository.existsByRequesterAndStatusAndDateRangeOverlap(nurseId, RequestStatus.APPROVED,
                                                                                    startDate, endDate))
        {
            throw new InvalidRequestException("There is already a vacation request approved for this date range");
        }
    }

    private void validatePendingStatus(VacationRequest request)
    {
        if(request.getStatus() != RequestStatus.PENDING)
        {
            throw new InvalidRequestException("Only the PENDING request could be modified");
        }
    }

    private void validateReviewerForApproval(VacationRequest request, RequestStatus newStatus, Long currentUserId)
    {
        if (newStatus == RequestStatus.APPROVED || newStatus == RequestStatus.REJECTED)
        {
            boolean isValidSupervisor = supervisorDepartmentRepository.existsByNurseIdAndSupervisorId(
                    request.getRequestingNurse().getId(),
                    currentUserId
            );

            if (!isValidSupervisor)
            {
                throw new InvalidRequestException("You are not the supervisor of this nurse's department");
            }
        }
    }
}