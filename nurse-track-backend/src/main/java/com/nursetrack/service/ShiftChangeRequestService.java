package com.nursetrack.service;

import com.nursetrack.domain.enums.NotificationType;
import com.nursetrack.domain.enums.ShiftStatus;
import com.nursetrack.domain.enums.Status;
import com.nursetrack.domain.model.Shift;
import com.nursetrack.domain.model.ShiftChangeRequest;
import com.nursetrack.domain.model.SupervisorDepartment;
import com.nursetrack.domain.model.User;
import com.nursetrack.exception.ShiftChangeRequestNotFoundException;
import com.nursetrack.exception.SupervisorDepartmentNotFoundException;
import com.nursetrack.repository.ShiftChangeRequestRepository;
import com.nursetrack.repository.ShiftRepository;
import com.nursetrack.repository.SupervisorDepartmentRepository;
import com.nursetrack.utils.SendNotification;
import com.nursetrack.utils.ShiftChangeRequestValidation;
import com.nursetrack.web.dto.request.shiftChange.CreateShiftChangeRequest;
import com.nursetrack.web.dto.request.shiftChange.UpdateShiftChangeRequest;
import com.nursetrack.web.dto.response.ShiftChangeResponse;
import com.nursetrack.web.mappers.ShiftChangeRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ShiftChangeRequestService
{
    private final ShiftChangeRequestRepository shiftChangeRequestRepository;
    private final ShiftChangeRequestMapper shiftChangeRequestMapper;
    private final ShiftChangeRequestValidation validator;
    private final ShiftRepository shiftRepository;
    private final SupervisorDepartmentRepository supervisorDepartmentRepository;
    private final SendNotification sendNotification;

    // ========== CRUD Operations ==========
    public ShiftChangeResponse createShiftChangeRequest(CreateShiftChangeRequest request)
    {
        ShiftChangeRequest shiftChangeRequest = shiftChangeRequestMapper.toEntity(request);
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

    public ShiftChangeResponse updateShiftChangeRequestStatus(Long requestId, UpdateShiftChangeRequest request)
    {
        ShiftChangeRequest shiftChangeRequest = shiftChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ShiftChangeRequestNotFoundException(requestId));

        validator.validateShiftChangeUpdate(shiftChangeRequest, request);
        shiftChangeRequestMapper.updateModel(request, shiftChangeRequest);

        if (request.getStatus() == Status.APPROVED)
        {
            executeShiftChangeSwap(shiftChangeRequest);
        }

        sendNotification.notifyStatusChange(shiftChangeRequest.getRequestingNurse().getId(),
                                            shiftChangeRequest.getReceivingNurse().getId(),
                                            request.getStatus(),
                                            request.getReviewedNotes(),
                                            NotificationType.SHIFT_CHANGE);

        return shiftChangeRequestMapper.toDTO(shiftChangeRequestRepository.save(shiftChangeRequest));
    }

    // ========== Query Operations ==========
    public ShiftChangeResponse getShiftChangeRequestById(Long requestId, Long currentUserId)
    {
        ShiftChangeRequest shiftChangeRequest = shiftChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ShiftChangeRequestNotFoundException(requestId));

        if (!shiftChangeRequest.getRequestingNurse().getId().equals(currentUserId))
        {
            throw new AccessDeniedException("You can only access your own requests");
        }

        return shiftChangeRequestMapper.toDTO(shiftChangeRequest);
    }

    public List<ShiftChangeResponse> getShiftChangeRequestsByNurse(Long nurseId)
    {
        return shiftChangeRequestRepository.findByRequestingNurseId(nurseId)
                .stream()
                .map(shiftChangeRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ShiftChangeResponse> getReceivedShiftChangeRequests(Long nurseId)
    {
        return shiftChangeRequestRepository.findByReceivingNurseId(nurseId)
                .stream()
                .map(shiftChangeRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ShiftChangeResponse> getPendingShiftChangeRequestsByDepartment(Long departmentId)
    {
        return shiftChangeRequestRepository.findByDepartmentAndStatus(departmentId, Status.PENDING)
                .stream()
                .map(shiftChangeRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ShiftChangeResponse> getShiftChangeRequestsByDepartment(Long departmentId)
    {
        return shiftChangeRequestRepository.findByDepartment(departmentId)
                .stream()
                .map(shiftChangeRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ========== Business Logic ==========
    private User getSupervisor(CreateShiftChangeRequest request)
    {
        Shift offeredShift = shiftRepository.findById(request.getOfferedShiftId()).orElseThrow();

        return supervisorDepartmentRepository.findByDepartmentId(offeredShift.getDepartment().getId())
                .map(SupervisorDepartment::getSupervisor)
                .orElseThrow(() -> new SupervisorDepartmentNotFoundException(offeredShift.getDepartment().getId()));
    }

    private void executeShiftChangeSwap(ShiftChangeRequest request)
    {
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