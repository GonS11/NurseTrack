package com.nurse_track_back.nurse_track_back.web.dto.request.notification;

import com.nurse_track_back.nurse_track_back.domain.enums.NotificationType;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidUserId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotificationRequest
{
    @NotNull(message = "User ID is required")
    @ValidUserId(message = "Invalid user ID")
    private Long userId;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;
}
