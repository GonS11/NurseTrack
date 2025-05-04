package com.nursetrack.service;

import com.nursetrack.domain.enums.NotificationType;
import com.nursetrack.domain.enums.ShiftStatus;
import com.nursetrack.domain.enums.Status;
import com.nursetrack.domain.model.*;
import com.nursetrack.exception.ShiftChangeRequestNotFoundException;
import com.nursetrack.exception.SupervisorDepartmentNotFoundException;
import com.nursetrack.repository.ShiftChangeRequestRepository;
import com.nursetrack.repository.ShiftRepository;
import com.nursetrack.repository.SupervisorDepartmentRepository;
import com.nursetrack.utils.ShiftChangeRequestValidation;
import com.nursetrack.web.dto.request.notification.CreateNotificationRequest;
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
    private final NotificationService notificationService;

    public ShiftChangeResponse createShiftChangeRequest(CreateShiftChangeRequest request)
    {
        ShiftChangeRequest shiftChangeRequest = shiftChangeRequestMapper.toEntity(request);
        shiftChangeRequest.setIsInterchange(true);
        shiftChangeRequest.setReviewedBy(getSupervisor(request));

        validator.validateShiftChangeCreation(request);

        ShiftChangeRequest savedRequest = requestRepository.save(shiftChangeRequest);
        notifyAll(savedRequest);

        return shiftChangeRequestMapper.toDTO(savedRequest);
    }

    public ShiftChangeResponse updateShiftChangeRequestStatus(Long requestId, UpdateShiftChangeRequest request)
    {
        ShiftChangeRequest shiftChangeRequest = getExistingRequest(requestId);
        validator.validateShiftChangeUpdate(shiftChangeRequest, request);

        shiftChangeRequestMapper.updateModel(request, shiftChangeRequest);

        if (request.getStatus() == Status.APPROVED) executeShiftChangeSwap(shiftChangeRequest);

        notifyStatusChange(shiftChangeRequest, request);

        return shiftChangeRequestMapper.toDTO(requestRepository.save(shiftChangeRequest));
    }


    private User getSupervisor(CreateShiftChangeRequest request)
    {
        Shift offeredShift = shiftRepository.findById(request.getOfferedShiftId()).orElseThrow();

        return supervisorDepartmentRepository.findByDepartmentId(offeredShift.getDepartment().getId())
                .map(SupervisorDepartment::getSupervisor)
                .orElseThrow(() -> new SupervisorDepartmentNotFoundException(offeredShift.getDepartment().getId()));
    }

    private void notifyAll(ShiftChangeRequest request)
    {
        sendNotification(request.getRequestingNurse().getId(), NotificationType.SHIFT_CHANGE,
                         "Shift Change Request Created",
                         "Your shift change request has been submitted and is pending review");

        sendNotification(request.getReceivingNurse().getId(), NotificationType.SHIFT_CHANGE,
                         "Shift Change Request Received",
                         "You've received a shift change request from a colleague");

        sendNotification(request.getReviewedBy().getId(), NotificationType.SHIFT_CHANGE,
                         "New Shift Change Request",
                         "A new shift change request requires your review");
    }

    private void notifyStatusChange(ShiftChangeRequest request, UpdateShiftChangeRequest statusRequest)
    {
        String requesterMessage = buildStatusMessage(statusRequest, true);
        String receiverMessage = buildStatusMessage(statusRequest, false);

        sendNotification(request.getRequestingNurse().getId(),
                         NotificationType.SHIFT_CHANGE,
                         "Shift Change Request Update",
                         requesterMessage);

        sendNotification(request.getReceivingNurse().getId(),
                         NotificationType.SHIFT_CHANGE,
                         "Shift Change Request Update",
                         receiverMessage);
    }

    private void sendNotification(Long userId, NotificationType type, String title, String message)
    {
        CreateNotificationRequest notification = new CreateNotificationRequest(userId, type, title, message);

        notificationService.createNotification(notification);
    }

    private ShiftChangeRequest getExistingRequest(Long requestId)
    {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new ShiftChangeRequestNotFoundException(requestId));
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

    private String buildStatusMessage(UpdateShiftChangeRequest request, boolean isRequester)
    {
        String prefix = isRequester ? "[REQUESTER] " : "[RECEIVER] ";
        String statusMessage = switch (request.getStatus()) {
            case APPROVED -> "The shift change has been APPROVED";
            case REJECTED -> "The shift change has been REJECTED. Reason: " + request.getReviewedNotes();
            case CANCELLED -> "The shift change has been CANCELLED. Reason: " + request.getReviewedNotes();
            default -> "Status updated: " + request.getStatus();
        };

        return prefix + statusMessage;
    }
}

}