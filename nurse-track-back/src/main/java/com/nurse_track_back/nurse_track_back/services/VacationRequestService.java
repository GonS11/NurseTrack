package com.nurse_track_back.nurse_track_back.services;

import com.nurse_track_back.nurse_track_back.domain.enums.NotificationType;
import com.nurse_track_back.nurse_track_back.domain.enums.RequestStatus;
import com.nurse_track_back.nurse_track_back.domain.models.Shift;
import com.nurse_track_back.nurse_track_back.domain.models.User;
import com.nurse_track_back.nurse_track_back.domain.models.VacationRequest;
import com.nurse_track_back.nurse_track_back.exceptions.ResourceNotFoundException;
import com.nurse_track_back.nurse_track_back.repositories.ShiftRepository;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.repositories.VacationRequestRepository;
import com.nurse_track_back.nurse_track_back.utils.SendNotification;
import com.nurse_track_back.nurse_track_back.utils.VacationRequestValidation;
import com.nurse_track_back.nurse_track_back.web.dto.request.vacationRequest.CreateVacationRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.vacationRequest.UpdateVacationRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.VacationRequestResponse;
import com.nurse_track_back.nurse_track_back.web.mappers.VacationRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class VacationRequestService {
    private final VacationRequestRepository vacationRequestRepository;
    private final VacationRequestMapper vacationRequestMapper;
    private final VacationRequestValidation validator;
    private final UserRepository userRepository;
    private final SendNotification sendNotification;
    private final ShiftRepository shiftRepository;

    // ========== CRUD Operations ==========
    public VacationRequestResponse createVacationRequest(CreateVacationRequest request) {
        VacationRequest vacationRequest = vacationRequestMapper.toEntity(request, userRepository);
        validator.validateVacationRequestCreation(request);

        VacationRequest savedRequest = vacationRequestRepository.save(vacationRequest);

        sendNotification.notifyCreation(
                savedRequest.getRequestingNurse().getId(),
                null,
                savedRequest.getReviewedBy() != null ? savedRequest.getReviewedBy().getId() : null,
                NotificationType.VACATION_REQUEST);

        return vacationRequestMapper.toDTO(savedRequest);
    }

    public VacationRequestResponse updateVacationRequestStatus(Long requestId,
            UpdateVacationRequest request,
            Long currentUserId) {
        VacationRequest vacationRequest = vacationRequestRepository.findById(requestId)
                .orElseThrow(() -> ResourceNotFoundException.create("Vacation request", requestId));

        // Validar reglas de negocio
        validator.validateVacationRequestUpdate(vacationRequest, request, currentUserId);

        // Asignar campos automáticamente después de la validación
        if (request.getStatus() == RequestStatus.APPROVED
                || request.getStatus() == RequestStatus.REJECTED) {
            User reviewer = userRepository.findById(currentUserId)
                    .orElseThrow(() -> ResourceNotFoundException.create("User", currentUserId));

            vacationRequest.setReviewedBy(reviewer);
            vacationRequest.setReviewedAt(LocalDateTime.now());
        }

        // Actualizar campos del DTO
        vacationRequestMapper.updateModel(request, vacationRequest, userRepository);

        // Notificar cambios
        sendNotification.notifyStatusChange(
                vacationRequest.getRequestingNurse().getId(),
                null,
                request.getStatus(),
                request.getReviewedNotes(),
                NotificationType.VACATION_REQUEST);

        // Guardar y procesar conflictos
        VacationRequest updatedRequest = vacationRequestRepository.save(vacationRequest);

        if (updatedRequest.getStatus() == RequestStatus.APPROVED) {
            checkShiftConflicts(updatedRequest);
        }

        return vacationRequestMapper.toDTO(updatedRequest);
    }

    // ========== Query Operations ==========
    public VacationRequestResponse getVacationRequestById(Long requestId, Long currentUserId) {
        VacationRequest vacationRequest = vacationRequestRepository.findById(requestId)
                .orElseThrow(() -> ResourceNotFoundException.create("Vacation request", requestId));

        // Verify the current user has access to this request
        if (!vacationRequest.getRequestingNurse().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You can only access your own requests");
        }

        return vacationRequestMapper.toDTO(vacationRequest);
    }

    public List<VacationRequestResponse> getVacationRequestsByNurse(Long nurseId) {
        return vacationRequestRepository.findByRequestingNurseId(nurseId)
                .stream()
                .map(vacationRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<VacationRequestResponse> getPendingVacationRequestsByDepartment(Long departmentId, Long currentUserId) {
        return vacationRequestRepository.findByDepartmentAndStatus(departmentId, RequestStatus.PENDING)
                .stream()
                .map(vacationRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<VacationRequestResponse> getVacationRequestsByDepartment(Long departmentId) {
        return vacationRequestRepository.findByDepartment(departmentId)
                .stream()
                .map(vacationRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ========== Business Logic ==========
    private void checkShiftConflicts(VacationRequest request) {
        List<Shift> conflictingShifts = shiftRepository.findAllByNurseIdAndShiftDateBetween(
                request.getRequestingNurse().getId(),
                request.getStartDate(),
                request.getEndDate());

        if (!conflictingShifts.isEmpty()) {
            throw new IllegalStateException("Approved vacation conflicts with existing shifts");
        }
    }

}