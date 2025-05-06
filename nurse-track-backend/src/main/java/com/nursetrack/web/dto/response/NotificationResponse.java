package com.nursetrack.web.dto.response;

import com.nursetrack.domain.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse
{
    private Long id;
    private UserSimpleResponse user;
    private NotificationType notificationType;
    private String title;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
    // private String timeAgo; //CALCULAR EN EL FRONT
}