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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ShiftChangeRequestService
{
    private final ShiftChangeRequestRepository requestRepository;
    private final ShiftChangeRequestMapper shiftChangeRequestMapper;
    private final ShiftChangeRequestValidation validator;
    private final ShiftRepository shiftRepository;
    private final SupervisorDepartmentRepository supervisorDepartmentRepository;
    private final SendNotification sendNotification;

    public ShiftChangeResponse createShiftChangeRequest(CreateShiftChangeRequest request)
    {
        ShiftChangeRequest shiftChangeRequest = shiftChangeRequestMapper.toEntity(request);
        shiftChangeRequest.setIsInterchange(true);
        shiftChangeRequest.setReviewedBy(getSupervisor(request));

        validator.validateShiftChangeCreation(request);

        ShiftChangeRequest savedRequest = requestRepository.save(shiftChangeRequest);

        //Notificacion
        sendNotification.notifyCreation(
                savedRequest.getRequestingNurse().getId(),
                savedRequest.getReceivingNurse().getId(),
                savedRequest.getReviewedBy().getId(),
                NotificationType.SHIFT_CHANGE
        );

        return shiftChangeRequestMapper.toDTO(savedRequest);
    }

    public ShiftChangeResponse updateShiftChangeRequestStatus(Long requestId, UpdateShiftChangeRequest request)
    {
        ShiftChangeRequest shiftChangeRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new ShiftChangeRequestNotFoundException(requestId));

        validator.validateShiftChangeUpdate(shiftChangeRequest, request);
        shiftChangeRequestMapper.updateModel(request, shiftChangeRequest);

        if (request.getStatus() == Status.APPROVED)
        {
            executeShiftChangeSwap(shiftChangeRequest);
        }

        sendNotification.notifyStatusChange(
                shiftChangeRequest.getRequestingNurse().getId(),
                shiftChangeRequest.getReceivingNurse().getId(),
                request.getStatus(),
                request.getReviewedNotes(),
                NotificationType.SHIFT_CHANGE
        );

        return shiftChangeRequestMapper.toDTO(requestRepository.save(shiftChangeRequest));
    }


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

        // Swap nurses
        User tempNurse = offeredShift.getNurse();
        offeredShift.setNurse(desiredShift.getNurse());
        desiredShift.setNurse(tempNurse);

        // Mark as swapped
        offeredShift.setStatus(ShiftStatus.SWAPPED);
        desiredShift.setStatus(ShiftStatus.SWAPPED);

        shiftRepository.saveAll(List.of(offeredShift, desiredShift));
    }
}