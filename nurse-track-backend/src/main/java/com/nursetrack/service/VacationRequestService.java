package com.nursetrack.service;

import com.nursetrack.domain.enums.NotificationType;
import com.nursetrack.domain.enums.Status;
import com.nursetrack.domain.model.Shift;
import com.nursetrack.domain.model.VacationRequest;
import com.nursetrack.exception.VacationNotFoundException;
import com.nursetrack.repository.ShiftRepository;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.repository.VacationRequestRepository;
import com.nursetrack.utils.SendNotification;
import com.nursetrack.utils.VacationRequestValidation;
import com.nursetrack.web.dto.request.vacationRequest.CreateVacationRequest;
import com.nursetrack.web.dto.request.vacationRequest.UpdateVacationRequest;
import com.nursetrack.web.dto.response.VacationRequestResponse;
import com.nursetrack.web.mappers.VacationRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class VacationRequestService
{
    private final VacationRequestRepository vacationRequestRepository;
    private final VacationRequestMapper vacationRequestMapper;
    private final VacationRequestValidation validator;
    private final UserRepository userRepository;
    private final SendNotification sendNotification;
    private final ShiftRepository shiftRepository;

    // ========== CRUD Operations ==========
    public VacationRequestResponse createVacationRequest(CreateVacationRequest request)
    {
        VacationRequest vacationRequest = vacationRequestMapper.toEntity(request);
        validator.validateVacationRequestCreation(request);

        VacationRequest savedRequest = vacationRequestRepository.save(vacationRequest);

        sendNotification.notifyCreation(
                savedRequest.getRequestingNurse().getId(),
                null,
                savedRequest.getReviewedBy() != null ? savedRequest.getReviewedBy().getId() : null,
                NotificationType.VACATION_REQUEST
        );

        return vacationRequestMapper.toDTO(savedRequest);
    }

    public VacationRequestResponse updateVacationRequestStatus(Long requestId, UpdateVacationRequest request, Long currentUserId)
    {
        VacationRequest vacationRequest = vacationRequestRepository.findById(requestId)
                .orElseThrow(() -> new VacationNotFoundException(requestId));

        if (!vacationRequest.getRequestingNurse().getId().equals(currentUserId)
                || !vacationRequest.getReviewedBy().getId().equals(currentUserId))
        {
            throw new AccessDeniedException("You do not have access");
        }

        validator.validateVacationRequestUpdate(vacationRequest, request);
        vacationRequestMapper.updateModel(request, vacationRequest, userRepository);

        sendNotification.notifyStatusChange(
                vacationRequest.getRequestingNurse().getId(),
                null,
                request.getStatus(),
                request.getReviewedNotes(),
                NotificationType.VACATION_REQUEST
        );

        VacationRequest updatedRequest = vacationRequestRepository.save(vacationRequest);

        if (updatedRequest.getStatus() == Status.APPROVED)
        {
            checkShiftConflicts(updatedRequest);
        }

        return vacationRequestMapper.toDTO(updatedRequest);
    }

    // ========== Query Operations ==========
    public VacationRequestResponse getVacationRequestById(Long requestId, Long currentUserId)
    {
        VacationRequest vacationRequest = vacationRequestRepository.findById(requestId)
                .orElseThrow(() -> new VacationNotFoundException(requestId));

        // Verify the current user has access to this request
        if (!vacationRequest.getRequestingNurse().getId().equals(currentUserId))
        {
            throw new AccessDeniedException("You can only access your own requests");
        }

        return vacationRequestMapper.toDTO(vacationRequest);
    }

    public List<VacationRequestResponse> getVacationRequestsByNurse(Long nurseId)
    {
        return vacationRequestRepository.findByRequestingNurseId(nurseId)
                .stream()
                .map(vacationRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<VacationRequestResponse> getPendingVacationRequestsByDepartment(Long departmentId, Long currentUserId)
    {
        return vacationRequestRepository.findByDepartmentAndStatus(departmentId, Status.PENDING)
                .stream()
                .map(vacationRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<VacationRequestResponse> getVacationRequestsByDepartment(Long departmentId)
    {
        return vacationRequestRepository.findByDepartment(departmentId)
                .stream()
                .map(vacationRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ========== Business Logic ==========
    private void checkShiftConflicts(VacationRequest request)
    {
        List<Shift> conflictingShifts = shiftRepository.findAllByNurseIdAndShiftDateBetween(request.getRequestingNurse().getId(),
                                                                                            request.getStartDate(),
                                                                                            request.getEndDate());

        if (!conflictingShifts.isEmpty())
        {
            throw new IllegalStateException("Approved vacation conflicts with existing shifts");
        }
    }


}