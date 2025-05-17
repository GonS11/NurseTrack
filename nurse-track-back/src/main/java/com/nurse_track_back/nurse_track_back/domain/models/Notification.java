package com.nurse_track_back.nurse_track_back.domain.models;

import com.nurse_track_back.nurse_track_back.domain.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notifications_user", columnList = "user_id, is_read"),
        @Index(name = "idx_notifications_type", columnList = "type")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('SHIFT_CHANGE', 'VACATION_REQUEST', 'GENERAL', 'SYSTEM', 'EMERGENCY') DEFAULT 'GENERAL'")
    private NotificationType type;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String message;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}