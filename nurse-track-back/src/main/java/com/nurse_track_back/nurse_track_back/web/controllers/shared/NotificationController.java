package com.nurse_track_back.nurse_track_back.web.controllers.shared;

import com.nurse_track_back.nurse_track_back.services.NotificationService;
import com.nurse_track_back.nurse_track_back.web.dto.request.notification.CreateNotificationRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.NotificationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/notifications")
@RequiredArgsConstructor
public class NotificationController
{
    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("#userId == principal.id")
    public ResponseEntity<Page<NotificationResponse>> getAllNotifications(@PathVariable("userId") Long userId,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam(defaultValue = "createdAt") String sortBy)
    {
        Page<NotificationResponse> pageDto = notificationService.getAllUserNotifications(userId, page, size, sortBy);
        return ResponseEntity.ok(pageDto);
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

    @PutMapping("/{notificationId}/read")
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