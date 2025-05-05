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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    public VacationRequestResponse createVacationRequest(CreateVacationRequest request)
    {
        VacationRequest vacationRequest = vacationRequestMapper.toEntity(request);
        validator.validateVacationRequestCreation(request);

        VacationRequest savedRequest = vacationRequestRepository.save(vacationRequest);

        sendNotification.notifyCreation(
                savedRequest.getRequestingNurse().getId(),
                null, // No receiver for vacation
                savedRequest.getReviewedBy() != null ? savedRequest.getReviewedBy().getId() : null,
                NotificationType.VACATION_REQUEST
        );

        return vacationRequestMapper.toDTO(savedRequest);
    }


    public VacationRequestResponse updateVacationRequestStatus(Long requestId, UpdateVacationRequest request)
    {
        VacationRequest vacationRequest = vacationRequestRepository.findById(requestId)
                .orElseThrow(() -> new VacationNotFoundException(requestId));

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

        // Si fue aprobado, verificar conflictos con turnos
        if (updatedRequest.getStatus() == Status.APPROVED)
        {
            checkShiftConflicts(updatedRequest);
        }

        return vacationRequestMapper.toDTO(updatedRequest);
    }

    private void checkShiftConflicts(VacationRequest request)
    {
        List<Shift> conflictingShifts = shiftRepository.findByNurseAndDateRange(
                request.getRequestingNurse(),
                request.getStartDate(),
                request.getEndDate());

        if (!conflictingShifts.isEmpty())
        {
            throw new IllegalStateException("Approved vacation conflicts with existing shifts");
        }
    }
}