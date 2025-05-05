package com.nursetrack.web.controller.shared;

import com.nursetrack.service.NotificationService;
import com.nursetrack.web.dto.request.notification.CreateNotificationRequest;
import com.nursetrack.web.dto.response.NotificationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/notifications")
@RequiredArgsConstructor
public class NotificationController
{
    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("#userId == principal.id")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@PathVariable("userId") Long userId)
    {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/{notificationId}")
    @PreAuthorize("#userId == principal.id")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable("userId") Long userId,
                                                                @PathVariable("notificationId") Long notificationId)
    {
        return ResponseEntity.ok(notificationService.getNotification(notificationId, userId));
    }

    @PostMapping
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')")
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody CreateNotificationRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(request));
    }

    @PatchMapping("/{notificationId}/read")
    @PreAuthorize("#userId == principal.id")
    public ResponseEntity<Void> markAsRead(@PathVariable("userId") Long userId,
                                           @PathVariable("notificationId") Long notificationId)
    {
        notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNotification(@PathVariable("userId") Long userId,
                                                   @PathVariable("notificationId") Long notificationId)
    {
        notificationService.deleteNotification(notificationId, userId);
        return ResponseEntity.noContent().build();
    }
}