package com.nursetrack.web.dto.request.notification;

import com.nursetrack.domain.enums.NotificationType;
import com.nursetrack.validations.annotations.ValidUserId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequest
{
    @NotNull @ValidUserId
    private Long userId;

    @NotNull
    private NotificationType type;

    @NotBlank @Size(max = 100)
    private String title;

    @NotBlank @Size(max = 1000)
    private String message;
}