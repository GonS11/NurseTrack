package com.nurse_track_back.nurse_track_back.services;

import com.nurse_track_back.nurse_track_back.domain.enums.NotificationType;
import com.nurse_track_back.nurse_track_back.domain.enums.RequestStatus;
import com.nurse_track_back.nurse_track_back.domain.enums.ShiftStatus;
import com.nurse_track_back.nurse_track_back.domain.models.Shift;
import com.nurse_track_back.nurse_track_back.domain.models.ShiftChangeRequest;
import com.nurse_track_back.nurse_track_back.domain.models.SupervisorDepartment;
import com.nurse_track_back.nurse_track_back.domain.models.User;
import com.nurse_track_back.nurse_track_back.exceptions.ResourceNotFoundException;
import com.nurse_track_back.nurse_track_back.repositories.ShiftChangeRequestRepository;
import com.nurse_track_back.nurse_track_back.repositories.ShiftRepository;
import com.nurse_track_back.nurse_track_back.repositories.SupervisorDepartmentRepository;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.utils.SendNotification;
import com.nurse_track_back.nurse_track_back.utils.ShiftChangeRequestValidation;
import com.nurse_track_back.nurse_track_back.web.dto.request.shiftChange.CreateShiftChangeRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.shiftChange.UpdateShiftChangeRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.ShiftChangeResponse;
import com.nurse_track_back.nurse_track_back.web.mappers.ShiftChangeRequestMapper;
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
public class ShiftChangeRequestService {
    private final ShiftChangeRequestRepository shiftChangeRequestRepository;
    private final ShiftChangeRequestMapper shiftChangeRequestMapper;
    private final ShiftChangeRequestValidation validator;
    private final ShiftRepository shiftRepository;
    private final SupervisorDepartmentRepository supervisorDepartmentRepository;
    private final SendNotification sendNotification;
    private final UserRepository userRepository;

    // ========== CRUD Operations ==========
    public ShiftChangeResponse createShiftChangeRequest(CreateShiftChangeRequest request) {
        ShiftChangeRequest shiftChangeRequest = shiftChangeRequestMapper.toEntity(request, userRepository,
                shiftRepository);
        shiftChangeRequest.setIsInterchange(true);
        shiftChangeRequest.setReviewedBy(getSupervisor(request));

        validator.validateShiftChangeCreation(request);

        ShiftChangeRequest savedRequest = shiftChangeRequestRepository.save(shiftChangeRequest);

        sendNotification.notifyCreation(savedRequest.getRequestingNurse().getId(),
                savedRequest.getReceivingNurse().getId(),
                savedRequest.getReviewedBy().getId(),
                NotificationType.SHIFT_CHANGE);

        return shiftChangeRequestMapper.toDTO(savedRequest);
    }

    public ShiftChangeResponse updateShiftChangeRequestStatus(Long requestId,
            UpdateShiftChangeRequest request,
            Long currentUserId) {
        ShiftChangeRequest shiftChangeRequest = shiftChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> ResourceNotFoundException.create("Shift change request", requestId));

        validator.validateShiftChangeUpdate(shiftChangeRequest, request, currentUserId);

        // Asignar campos
        if (request.getStatus() == RequestStatus.APPROVED
                || request.getStatus() == RequestStatus.REJECTED) {
            User reviewer = userRepository.findById(currentUserId)
                    .orElseThrow(() -> ResourceNotFoundException.create("User", currentUserId));

            shiftChangeRequest.setReviewedBy(reviewer);
            shiftChangeRequest.setReviewedAt(LocalDateTime.now());

        }

        // Actualizacion campos
        shiftChangeRequestMapper.updateModel(request, shiftChangeRequest);

        if (request.getStatus() == RequestStatus.APPROVED) {
            executeShiftChangeSwap(shiftChangeRequest);
        }

        sendNotification.notifyStatusChange(shiftChangeRequest.getRequestingNurse().getId(),
                shiftChangeRequest.getReceivingNurse().getId(),
                request.getStatus(),
                request.getReviewedNotes(),
                NotificationType.SHIFT_CHANGE);

        return shiftChangeRequestMapper.toDTO(shiftChangeRequestRepository.save(shiftChangeRequest));
    }

    public ShiftChangeResponse getShiftChangeRequestById(Long requestId, Long currentUserId) {
        ShiftChangeRequest shiftChangeRequest = shiftChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> ResourceNotFoundException.create("Shift change request", requestId));

        if (!shiftChangeRequest.getRequestingNurse().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You can only access your own requests");
        }

        return shiftChangeRequestMapper.toDTO(shiftChangeRequest);
    }

    public List<ShiftChangeResponse> getShiftChangeRequestsByNurse(Long nurseId) {
        return shiftChangeRequestRepository.findByRequestingNurseId(nurseId)
                .stream()
                .map(shiftChangeRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ShiftChangeResponse> getReceivedShiftChangeRequests(Long nurseId) {
        return shiftChangeRequestRepository.findByReceivingNurseId(nurseId)
                .stream()
                .map(shiftChangeRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ShiftChangeResponse> getPendingShiftChangeRequestsByDepartment(Long departmentId) {
        return shiftChangeRequestRepository.findByDepartmentAndStatus(departmentId, RequestStatus.PENDING)
                .stream()
                .map(shiftChangeRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ShiftChangeResponse> getShiftChangeRequestsByDepartment(Long departmentId) {
        return shiftChangeRequestRepository.findByDepartment(departmentId)
                .stream()
                .map(shiftChangeRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ========== Business Logic ==========
    private User getSupervisor(CreateShiftChangeRequest request) {
        Shift offeredShift = shiftRepository.findById(request.getOfferedShiftId()).orElseThrow();

        return supervisorDepartmentRepository.findByDepartment_Id(offeredShift.getDepartment().getId())
                .map(SupervisorDepartment::getSupervisor)
                .orElseThrow(
                        () -> ResourceNotFoundException.create("Department", offeredShift.getDepartment().getId()));
    }

    private void executeShiftChangeSwap(ShiftChangeRequest request) {
        Shift offeredShift = request.getOfferedShift();
        Shift desiredShift = request.getDesiredShift();

        User tempNurse = offeredShift.getNurse();
        offeredShift.setNurse(desiredShift.getNurse());
        desiredShift.setNurse(tempNurse);

        offeredShift.setStatus(ShiftStatus.SWAPPED);
        desiredShift.setStatus(ShiftStatus.SWAPPED);

        shiftRepository.saveAll(List.of(offeredShift, desiredShift));
    }
}