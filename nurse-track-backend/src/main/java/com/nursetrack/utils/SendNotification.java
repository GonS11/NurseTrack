package com.nursetrack.utils;

import com.nursetrack.domain.enums.NotificationType;
import com.nursetrack.domain.enums.Status;
import com.nursetrack.service.NotificationService;
import com.nursetrack.web.dto.request.notification.CreateNotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendNotification {

    @Autowired
    private NotificationService notificationService;

    // Notificación para creación de solicitud
    public void notifyCreation(Long requesterId, Long receiverId, Long reviewerId, NotificationType type)
    {
        // Notificar al solicitante
        sendNotification(requesterId, getCreationTitle(type, true),
                         getCreationMessage(type,true), type);

        // Notificar al receptor (solo para shift change)
        if (receiverId != null)
        {
            sendNotification(receiverId,getCreationTitle(type, false),
                             getCreationMessage(type, false), type);
        }

        // Notificar al revisor
        if (reviewerId != null)
        {
            sendNotification(reviewerId,"New " + getRequestType(type) + " Request",
                             "A new request requires your review", type);
        }
    }

    // Notificación para cambio de estado
    public void notifyStatusChange(Long requesterId, Long receiverId, Status status,
                                   String notes, NotificationType type)
    {
        // Notificar al solicitante
        sendNotification(requesterId,getRequestType(type) + " Request Update",
                         buildStatusMessage(status, notes, type, true), type);

        // Notificar al receptor (solo para shift change)
        if (receiverId != null)
        {
            sendNotification(receiverId,getRequestType(type) + " Request Update",
                             buildStatusMessage(status, notes, type, false), type);
        }
    }

    private String getRequestType(NotificationType type)
    {
        return type == NotificationType.VACATION_REQUEST ? "Vacation" : "Shift Change";
    }

    private String getCreationTitle(NotificationType type, boolean isRequester)
    {
        return isRequester
                ? getRequestType(type) + " Request Created"
                : "Shift Change Request Received";
    }

    private String getCreationMessage(NotificationType type, boolean isRequester)
    {
        return isRequester
                ? "Your request has been submitted and is pending review"
                : "You've received a shift change request from a colleague";
    }

    private String buildStatusMessage(Status status, String notes, NotificationType type, boolean isRequester)
    {
        String prefix = isRequester ? "Your " : "The ";
        String requestType = getRequestType(type).toLowerCase();

        return switch (status)
        {
            case APPROVED -> prefix + requestType + " request has been APPROVED";
            case REJECTED -> prefix + requestType + " request has been REJECTED. Reason: " + notes;
            case CANCELLED -> prefix + requestType + " request has been CANCELLED. Reason: " + notes;
            default -> prefix + requestType + " request status updated: " + status;
        };
    }

    private void sendNotification(Long userId, String title, String message, NotificationType type)
    {
        CreateNotificationRequest notification = new CreateNotificationRequest(userId, type, title, message);
        notificationService.createNotification(notification);
    }
}