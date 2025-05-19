package com.nurse_track_back.nurse_track_back.domain.models;

import com.nurse_track_back.nurse_track_back.domain.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "shift_change_requests", indexes = {
                @Index(name = "idx_shift_change_requester", columnList = "requester_id, status"),
                @Index(name = "idx_shift_change_timestamps", columnList = "created_at, reviewed_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftChangeRequest {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "requester_id", nullable = false)
        private User requestingNurse;

        @ManyToOne
        @JoinColumn(name = "offered_shift_id", nullable = false)
        private Shift offeredShift;

        @ManyToOne
        @JoinColumn(name = "recipient_id", nullable = false)
        private User receivingNurse;

        @ManyToOne
        @JoinColumn(name = "desired_shift_id", nullable = false)
        private Shift desiredShift;

        @Lob
        private String reason;

        @Lob
        @Column(name = "reviewed_notes")
        private String reviewedNotes;

        @Builder.Default
        @Enumerated(EnumType.STRING)
        @Column(columnDefinition = "ENUM('CANCELLED','PENDING','APPROVED','REJECTED') DEFAULT 'PENDING'")
        private RequestStatus status = RequestStatus.PENDING;

        @Builder.Default
        @Column(name = "is_interchange")
        private Boolean isInterchange = false;

        @ManyToOne
        @JoinColumn(name = "reviewed_by_id")
        private User reviewedBy;

        @Column(name = "reviewed_at")
        private LocalDateTime reviewedAt;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;
}