package com.nursetrack.domain.model;

import com.nursetrack.domain.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications",
        indexes = {
                @Index(name = "idx_notifications_user", columnList = "user_id, is_read"),
                @Index(name = "idx_notifications_type", columnList = "type")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false,
            columnDefinition = "ENUM('SHIFT_CHANGE', 'VACATION_REQUEST', 'GENERAL', 'SYSTEM', 'EMERGENCY')")
    private NotificationType type;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Transient
    public boolean isEmergency()
    {
        return type == NotificationType.EMERGENCY;
    }
}