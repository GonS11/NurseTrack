package com.nursetrack.domain;

import com.nursetrack.constants.AppEnums;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications",
        indexes = {
                @Index(name = "idx_notifications_user", columnList = "user_id, is_read"),
                @Index(name = "idx_notifications_type", columnList = "notification_type")
        })
@Getter
@Setter
@NoArgsConstructor
public class Notification
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User receivedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppEnums.NotificationType type = AppEnums.NotificationType.GENERAL;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String message;

    private Boolean isRead = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
